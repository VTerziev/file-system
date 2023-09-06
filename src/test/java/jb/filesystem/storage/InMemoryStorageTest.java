package jb.filesystem.storage;

import org.junit.Assert;
import org.junit.Test;

public class InMemoryStorageTest {

    @Test
    public void writeAndRead() {
        ByteStorage storage = new InMemoryStorage(5);
        byte[] toWrite = new byte[]{1,2,3};
        Assert.assertEquals(3, storage.write(1, 3, toWrite));
        byte[] toRead = new byte[3];
        Assert.assertEquals(3, storage.read(1, 3, toRead));

        Assert.assertTrue(equal(toWrite, toRead));
    }

    @Test
    public void multipleWrites() {
        ByteStorage storage = new InMemoryStorage(5);
        Assert.assertEquals(3, storage.write(1, 3, new byte[]{1, 1, 1}));
        Assert.assertEquals(2, storage.write(3, 3, new byte[]{2, 2, 2}));
        Assert.assertEquals(2, storage.write(0, 2, new byte[]{3, 3, 3}));

        byte[] toRead = new byte[5];
        Assert.assertEquals(5, storage.read(0, 5, toRead));
        Assert.assertTrue(equal(new byte[]{3, 3, 1, 2, 2}, toRead));
    }

    private boolean equal(byte[] arr1, byte[] arr2) {
        boolean result = true;
        for (int i = 0 ; i < arr1.length ; i ++ ) {
            result &= (arr1[i] == arr2[i]);
        }
        return result;
    }
}