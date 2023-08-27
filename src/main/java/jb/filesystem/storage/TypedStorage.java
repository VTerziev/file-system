package jb.filesystem.storage;

public abstract class TypedStorage<T> {

    private final ByteStorage storage;

    public TypedStorage (ByteStorage storage) {
        this.storage = storage;
    }

    public int read(int offset, int len, T[] buffer) {
        for (int i = offset ; i < offset+len ; i ++) {
            byte[] b = new byte[getByteSizeOfT()];
            int x = storage.read(i*getByteSizeOfT(), getByteSizeOfT(), b);
            if ( x != getByteSizeOfT() ) {
                throw new IllegalStateException(); // TODO: refactor?
            }
            buffer[i-offset] = decodeT(b);
        }
        // TODO: check if there are actually 'len' blocks
        return len;
    }

    public int write(int offset, int len, T[] buffer) {
        for (int i = offset ; i < offset+len ; i ++) {
            byte[] b = encodeT(buffer[i-offset]);
            int x = storage.write(i*getByteSizeOfT(), getByteSizeOfT(), b);
            if ( x != getByteSizeOfT() ) {
                throw new IllegalStateException(); // TODO: refactor?
            }
        }
        // TODO: check if there are actually 'len' blocks
        return len;
    }

    abstract int getByteSizeOfT();
    abstract T decodeT(byte[] bytes);
    abstract byte[] encodeT(T t);
}
