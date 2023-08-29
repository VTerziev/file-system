package jb.filesystem.storage;

/**
 * A class, representing view of an existing storage. Exposes only a sub-interval of the whole storage and makes it
 * look like it's the whole storage.
 */
public class StorageView implements ByteStorage {

    private final ByteStorage inner;
    private final long offset;
    private final long len;

    public StorageView(ByteStorage inner, long offset, long len) {
        this.inner = inner;
        this.offset = offset;
        this.len = len;
        if (offset+len > inner.getSize()) {
            throw new IllegalArgumentException("The view is not strictly inside the inner storage");
        }
    }

    @Override
    public long read(long offset, long len, byte[] buffer) {
        long newOffset = offset + this.offset;
        long newLen = Math.min(len, this.len + this.offset - newOffset);
        return inner.read(newOffset, newLen, buffer);
    }

    @Override
    public long write(long offset, long len, byte[] buffer) {
        long newOffset = offset + this.offset;
        long newLen = Math.min(len, this.len + this.offset - newOffset);
        return inner.write(newOffset, newLen, buffer);
    }

    @Override
    public long getSize() {
        return len;
    }
}
