package jb.filesystem.utils;

import jb.filesystem.storage.BitStorage;
import jb.filesystem.storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PersistentBitmask {
    private final int totalBlocks;
    private final BitSet freeBlocks;
    private final BitStorage storage;

    public PersistentBitmask(int totalBlocks, BitStorage storage) {
        this.totalBlocks = totalBlocks;
        this.storage = storage;
        freeBlocks = new BitSet(totalBlocks);
        for (int i = 0 ; i < totalBlocks ; i ++ ) {
            if (!storage.readBit(i)) {
                freeBlocks.set(i);
            }
        }
    }

    public int allocateBlock() {
        int freeBlock = freeBlocks.nextSetBit(0); // Find the first free block
        if (freeBlock != -1) {
            freeBlocks.clear(freeBlock); // Mark the block as used
            persistToStorage(freeBlock, true);
            return freeBlock;
        } else {
            throw new IllegalStateException("No available blocks.");
        }
    }

    public void deallocateBlock(int blockNumber) {
        if (blockNumber >= 0 && blockNumber < totalBlocks) {
            freeBlocks.set(blockNumber); // Mark the block as free
            persistToStorage(blockNumber, false);
        } else {
            throw new IllegalArgumentException("Invalid block number.");
        }
    }

    public boolean isAvailable(int blockNumber) {
        return !freeBlocks.get(blockNumber);
    }

    public int getAvailableBlocks() {
        return freeBlocks.cardinality();
    }

    public int getUsedBlocks() {
        return totalBlocks - freeBlocks.cardinality();
    }

    public static void main(String[] args) { // TODO: refactor as a test
        int n = 100;
        BitStorage storage = new BitStorage(new InMemoryStorage(n/8+1));
        storage.writeBit(42, true);
        PersistentBitmask bitmask = new PersistentBitmask(n, storage);

        // Allocate some blocks
        int block1 = bitmask.allocateBlock();
        int block2 = bitmask.allocateBlock();

        System.out.println("Allocated blocks: " + block1 + ", " + block2);
        System.out.println("Available blocks: " + bitmask.getAvailableBlocks());
        System.out.println("Used blocks: " + bitmask.getUsedBlocks());

        // Deallocate a block
        bitmask.deallocateBlock(block1);

        System.out.println("\nDeallocated block: " + block1);
        System.out.println("Available blocks: " + bitmask.getAvailableBlocks());
        System.out.println("Used blocks: " + bitmask.getUsedBlocks());

        List<Integer> arr = new ArrayList<>();
        for (int i = 0 ; i < 99 ; i ++ ) {
            arr.add(bitmask.allocateBlock());
            System.out.println(arr);
        }
    }

    private void persistToStorage(int blockNumber, boolean newValue) {
        boolean success = storage.writeBit(blockNumber, newValue);
        if (!success) {
            throw new IllegalStateException("Could not persist the bitmask");
        }
    }
}
