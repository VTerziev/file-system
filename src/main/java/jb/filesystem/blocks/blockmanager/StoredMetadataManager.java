package jb.filesystem.blocks.blockmanager;

import jb.filesystem.blocks.metadata.*;
import jb.filesystem.storage.MetadataStorage;
import jb.filesystem.utils.PersistentBitmask;

public class StoredMetadataManager implements MetadataBlocksManager {
    private final PersistentBitmask bitmask;
    private final MetadataStorage metadataStorage;

    public StoredMetadataManager(PersistentBitmask bitmask, MetadataStorage metadataStorage) {
        this.bitmask = bitmask;
        this.metadataStorage = metadataStorage;
    }

    @Override
    public synchronized MetadataBlock getBlock(int blockId) {
        if (bitmask.isAvailable(blockId)) {
            throw new IllegalArgumentException("Block " + blockId + " is not allocated");
        }
        MetadataBlock[] buffer = new MetadataBlock[1];
        metadataStorage.read(blockId, 1, buffer);
        return buffer[0];
    }

    @Override
    public synchronized DirectoryMetadata getDirectoryMetadata(int blockId) {
        return (DirectoryMetadata) getBlock(blockId);
    }

    @Override
    public synchronized FileMetadata getFileMetadata(int blockId) {
        return (FileMetadata) getBlock(blockId);
    }

    @Override
    public synchronized DataBlocksPointers getDataBlocksPointersMetadata(int blockId) {
        return (DataBlocksPointers) getBlock(blockId);
    }

    @Override
    public synchronized MetadataBlocksPointers getMetadataBlocksPointersMetadata(int blockId) {
        return (MetadataBlocksPointers) getBlock(blockId);
    }

    @Override
    public synchronized void saveBlock(int blockId, MetadataBlock block) {
        metadataStorage.write(blockId, 1, new MetadataBlock[]{block});
    }

    @Override
    public synchronized int allocateBlock() {
        int ret = bitmask.allocateBit();
        System.out.println("Allocating meta block: " + ret);
        return ret;
    }

    @Override
    public synchronized void deallocateBlock(int blockId) {
        System.out.println("Deallocating meta block: " + blockId);
        if (bitmask.isAvailable(blockId)) {
            throw new IllegalArgumentException("Block " + blockId + " is not allocated");
        }
        bitmask.deallocateBit(blockId);
    }
}
