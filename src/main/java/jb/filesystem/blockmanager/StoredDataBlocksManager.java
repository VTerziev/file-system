package jb.filesystem.blockmanager;

import jb.filesystem.data.DataBlock;
import jb.filesystem.storage.DataBlocksStorage;
import jb.filesystem.utils.PersistentBitmask;

// TODO: make concurrent
public class StoredDataBlocksManager implements DataBlocksManager {
    private final PersistentBitmask bitmask;
    private final DataBlocksStorage dataStorage;

    public StoredDataBlocksManager(PersistentBitmask bitmask, DataBlocksStorage dataStorage) {
        this.bitmask = bitmask;
        this.dataStorage = dataStorage;
    }

    @Override
    public DataBlock getBlock(int blockId) {
        DataBlock[] buffer = new DataBlock[1];
        dataStorage.read(blockId, 1, buffer);
        return buffer[0];
    }

    @Override
    public void saveBlock(int blockId, DataBlock block) {
        dataStorage.write(blockId, 1, new DataBlock[]{block});
    }

    @Override
    public int allocateBlock() {
        int ret = bitmask.allocateBlock();
        System.out.println("Allocating data block: " + ret);
        return ret;
    }

    @Override
    public void deallocateBlock(int blockId) {
        System.out.println("Deallocating data block: " + blockId);
        bitmask.deallocateBlock(blockId);
    }
}