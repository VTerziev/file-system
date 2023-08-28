package jb.filesystem.utils;

import jb.filesystem.storage.BitStorage;

import java.util.BitSet;

public class PersistentBitmask {
    private final int totalBits;
    private final BitSet freeBits;
    private final BitStorage storage;

    public PersistentBitmask(int totalBits, BitStorage storage) {
        this.totalBits = totalBits;
        this.storage = storage;
        freeBits = new BitSet(totalBits);
        for (int i = 0 ; i < totalBits ; i ++ ) {
            if (!storage.readBit(i)) {
                freeBits.set(i);
            }
        }
    }

    public int allocateBit() {
        int freeBit = freeBits.nextSetBit(0);
        if (freeBit != -1) {
            freeBits.clear(freeBit);
            persistToStorage(freeBit, true);
            return freeBit+1;
        } else {
            throw new IllegalStateException("No available bits.");
        }
    }

    public void deallocateBit(int bitNumber) {
        bitNumber --;
        if (bitNumber >= 0 && bitNumber < totalBits) {
            freeBits.set(bitNumber);
            persistToStorage(bitNumber, false);
        } else {
            throw new IllegalArgumentException("Invalid bit number.");
        }
    }

    public boolean isAvailable(int bitNumber) {
        return freeBits.get(bitNumber-1);
    }

    public int getAvailableBits() {
        return freeBits.cardinality();
    }

    public int getUsedBits() {
        return totalBits - freeBits.cardinality();
    }

    private void persistToStorage(int bitNumber, boolean newValue) {
        boolean success = storage.writeBit(bitNumber, newValue);
        if (!success) {
            throw new IllegalStateException("Could not persist the bitmask");
        }
    }
}
