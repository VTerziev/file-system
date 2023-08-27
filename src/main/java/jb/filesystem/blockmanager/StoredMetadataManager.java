package jb.filesystem.blockmanager;

import jb.filesystem.metadata.MetadataBlock;
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
    public MetadataBlock getBlock(int blockId) {
        MetadataBlock[] buffer = new MetadataBlock[1];
        metadataStorage.read(blockId, 1, buffer);
        return buffer[0];
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
        bitmask.deallocateBlock(blockId);
    }
}
