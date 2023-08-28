package jb.filesystem.storage;

import org.junit.Assert;
import org.junit.Test;

public class BitStorageTest {
    private static final boolean F = false;
    private static final boolean T = true;

    @Test
    public void writeOneBit() {
        BitStorage storage = new BitStorage(new InMemoryStorage(2));
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,F,F,F,F,F,F}, readWholeStorage(storage)));
        storage.writeBit(3, T);
        Assert.assertTrue(equal(new boolean[]{F,F,F,T,F,F,F,F,F,F}, readWholeStorage(storage)));
        storage.writeBit(3, F);
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,F,F,F,F,F,F}, readWholeStorage(storage)));
    }

    @Test
    public void writeMultipleBits() {
        BitStorage storage = new BitStorage(new InMemoryStorage(2));
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,F,F,F,F,F,F}, readWholeStorage(storage)));
        storage.writeBit(9, T);
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,F,F,F,F,F,T}, readWholeStorage(storage)));
        storage.writeBit(9, T);
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,F,F,F,F,F,T}, readWholeStorage(storage)));
        storage.writeBit(4, T);
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,T,F,F,F,F,T}, readWholeStorage(storage)));
        storage.writeBit(5, F);
        Assert.assertTrue(equal(new boolean[]{F,F,F,F,T,F,F,F,F, T}, readWholeStorage(storage)));
    }

    private boolean equal(boolean[] arr1, boolean[] arr2) {
        boolean result = true;
        for (int i = 0 ; i < arr1.length ; i ++ ) {
            result &= (arr1[i] == arr2[i]);
        }
        return result;
    }
    private boolean[] readWholeStorage(BitStorage storage) {
        boolean[] result = new boolean[storage.getSize()];
        for (int i = 0 ; i < storage.getSize() ; i ++ ) {
            result[i] = storage.readBit(i);
        }
        return result;
    }
}