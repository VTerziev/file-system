package jb.filesystem.storage;

public class StorageView implements ByteStorage {

    private final ByteStorage inner;
    private final int offset;
    private final int len;

    public StorageView(ByteStorage inner, int offset, int len) {
        this.inner = inner;
        this.offset = offset;
        this.len = len;
        if (offset+len > inner.getSize()) {
            throw new IllegalArgumentException("The view is not strictly inside the inner storage");
        }
    }

    @Override
    public int read(int offset, int len, byte[] buffer) {
        int newOffset = offset + this.offset;
        int newLen = Math.min(len, this.len + this.offset - newOffset);
        return inner.read(newOffset, newLen, buffer);
    }

    @Override
    public int write(int offset, int len, byte[] buffer) {
        int newOffset = offset + this.offset;
        int newLen = Math.min(len, this.len + this.offset - newOffset);
        if ( newLen < len ) {
            System.out.println("New len < len");
        }
        return inner.write(newOffset, newLen, buffer);
    }

    @Override
    public int getSize() {
        return len;
    }
}
