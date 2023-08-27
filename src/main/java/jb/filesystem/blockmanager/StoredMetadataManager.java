package jb.filesystem.blockmanager;

import jb.filesystem.metadata.*;
import jb.filesystem.storage.MetadataStorage;
import jb.filesystem.utils.PersistentBitmask;

public class StoredMetadataManager implements MetadataBlocksManager { // TODO: make singleton?
    private final PersistentBitmask bitmask;
    private final MetadataStorage metadataStorage;

    public StoredMetadataManager(PersistentBitmask bitmask, MetadataStorage metadataStorage) {
        this.bitmask = bitmask;
        this.metadataStorage = metadataStorage;
    }

    @Override
    public MetadataBlock getBlock(int blockId) {
        if (!bitmask.isAvailable(blockId)) {
            throw new IllegalArgumentException("Block " + blockId + " is not allocated");
        }
        MetadataBlock[] buffer = new MetadataBlock[1];
        metadataStorage.read(blockId, 1, buffer);
        return buffer[0];
    }

    @Override
    public DirectoryMetadata getDirectoryMetadata(int blockId) {
        return (DirectoryMetadata) getBlock(blockId);
    }

    @Override
    public FileMetadata getFileMetadata(int blockId) {
        return (FileMetadata) getBlock(blockId);
    }

    @Override
    public DataBlocksPointers getDataBlocksPointersMetadata(int blockId) {
        return (DataBlocksPointers) getBlock(blockId);
    }

    @Override
    public MetadataBlocksPointers getMetadataBlocksPointersMetadata(int blockId) {
        return (MetadataBlocksPointers) getBlock(blockId);
    }

    @Override
    public void saveBlock(int blockId, MetadataBlock block) {
        metadataStorage.write(blockId, 1, new MetadataBlock[]{block});
    }

    @Override
    public int allocateBlock() {
        int ret = bitmask.allocateBlock();
        System.out.println("Allocating meta block: " + ret);
        return ret;
    }

    @Override
    public void deallocateBlock(int blockId) {
        System.out.println("Deallocating meta block: " + blockId);
        if (!bitmask.isAvailable(blockId)) {
            throw new IllegalArgumentException("Block " + blockId + " is not allocated");
        }
        bitmask.deallocateBlock(blockId);
    }
}
