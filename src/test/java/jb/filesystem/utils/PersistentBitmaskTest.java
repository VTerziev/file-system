package jb.filesystem.utils;

import jb.filesystem.storage.BitStorage;
import jb.filesystem.storage.InMemoryStorage;
import org.junit.Assert;
import org.junit.Test;

public class PersistentBitmaskTest {

    @Test
    public void allocateOneBit() {
        int n = 8;
        BitStorage storage = new BitStorage(new InMemoryStorage(n/8));
        PersistentBitmask bitmask = new PersistentBitmask(n, storage);

        Assert.assertEquals(8, bitmask.getAvailableBits());

        int block1 = bitmask.allocateBit();
        Assert.assertEquals(1, block1);
        Assert.assertEquals(7, bitmask.getAvailableBits());
        Assert.assertFalse(bitmask.isAvailable(block1));
    }

    @Test
    public void allocateAllBits() {
        int n = 8;
        BitStorage storage = new BitStorage(new InMemoryStorage(n/8));
        PersistentBitmask bitmask = new PersistentBitmask(n, storage);

        for (int i = 0 ; i < 8 ; i ++ ) {
            bitmask.allocateBit();
        }
        Assert.assertEquals(0, bitmask.getAvailableBits());
    }

    @Test
    public void initiallyFullStorage() {
        int n = 8;
        BitStorage storage = new BitStorage(new InMemoryStorage(n/8));
        for (int i = 0 ; i < n  ; i ++ ) {
            storage.writeBit(i, true);
        }
        PersistentBitmask bitmask = new PersistentBitmask(n, storage);
        Assert.assertEquals(0, bitmask.getAvailableBits());

        for (int i = 1 ; i <= n; i ++ ) {
            bitmask.deallocateBit(i);
        }
        Assert.assertEquals(n, bitmask.getAvailableBits());
    }
}