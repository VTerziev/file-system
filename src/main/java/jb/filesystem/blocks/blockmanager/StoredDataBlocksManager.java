package jb.filesystem.blocks.blockmanager;

import jb.filesystem.blocks.data.DataBlock;
import jb.filesystem.storage.DataBlocksStorage;
import jb.filesystem.utils.PersistentBitmask;

public class StoredDataBlocksManager implements DataBlocksManager {
    private final PersistentBitmask bitmask;
    private final DataBlocksStorage dataStorage;

    public StoredDataBlocksManager(PersistentBitmask bitmask, DataBlocksStorage dataStorage) {
        this.bitmask = bitmask;
        this.dataStorage = dataStorage;
    }

    @Override
    public synchronized DataBlock getBlock(int blockId) {
        if (bitmask.isAvailable(blockId)) {
            throw new IllegalArgumentException("Block " + blockId + " is not allocated");
        }
        DataBlock[] buffer = new DataBlock[1];
        dataStorage.read(blockId, 1, buffer);
        return buffer[0];
    }

    @Override
    public synchronized void saveBlock(int blockId, DataBlock block) {
        dataStorage.write(blockId, 1, new DataBlock[]{block});
    }

    @Override
    public synchronized int allocateBlock() {
        return bitmask.allocateBit();
    }

    @Override
    public synchronized void deallocateBlock(int blockId) {
        if (bitmask.isAvailable(blockId)) {
            throw new IllegalArgumentException("Block " + blockId + " is not allocated");
        }
        bitmask.deallocateBit(blockId);
    }
}
