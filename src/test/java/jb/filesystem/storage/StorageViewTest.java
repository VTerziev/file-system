package jb.filesystem.storage;

import org.junit.Assert;
import org.junit.Test;

public class StorageViewTest {

    @Test
    public void propagatingToTheInnerStorage() {
        ByteStorage inner = new InMemoryStorage(4);
        StorageView view = new StorageView(inner, 1, 2);

        Assert.assertEquals(2, view.getSize());
        Assert.assertEquals(1, view.write(0, 1, new byte[]{42}));
        byte[] buffer = new byte[1];
        Assert.assertEquals(1, view.read(0, 1, buffer));
        Assert.assertEquals(42, buffer[0]);

        // Writing a byte is propagated to the inner storage
        Assert.assertEquals(1, inner.read(1, 1, buffer));
        Assert.assertEquals(42, buffer[0]);
    }

    @Test
    public void notWritingOutsideTheView() {
        ByteStorage inner = new InMemoryStorage(4);
        StorageView view = new StorageView(inner, 1, 2);

        Assert.assertEquals(2, view.getSize());
        // Try to write outside the view
        Assert.assertEquals(2, view.write(0, 3, new byte[]{42, 42, 42}));
        byte[] buffer = new byte[4];
        Assert.assertEquals(2, view.read(0, 2, buffer));
        Assert.assertEquals(42, buffer[0]);
        Assert.assertEquals(42, buffer[1]);

        // Nothing was written outside the view
        Assert.assertEquals(4, inner.read(0, 4, buffer));
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(42, buffer[1]);
        Assert.assertEquals(42, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidConstruction() {
        ByteStorage inner = new InMemoryStorage(4);
        StorageView view = new StorageView(inner, 1, 10);
    }

}