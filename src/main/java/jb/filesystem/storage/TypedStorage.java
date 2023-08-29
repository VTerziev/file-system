package jb.filesystem.storage;

/**
 * A storage, which works with some predetermined type. Requires all the instances of the type to be encoded with the
 * same number of bytes.
 * @param <T>
 */
public abstract class TypedStorage<T> {

    private final ByteStorage storage;

    public TypedStorage (ByteStorage storage) {
        this.storage = storage;
    }

    public long read(long offset, long len, T[] buffer) {
        for (long i = offset ; i < offset+len ; i ++) {
            byte[] b = new byte[getByteSizeOfT()];
            long x = storage.read(i*getByteSizeOfT(), getByteSizeOfT(), b);
            if ( x != getByteSizeOfT() ) {
                throw new IllegalStateException("Could not read the requested number of bytes");
            }
            buffer[(int)(i-offset)] = decodeT(b);
        }
        return len;
    }

    public long write(long offset, int len, T[] buffer) {
        for (long i = offset ; i < offset+len ; i ++) {
            byte[] b = encodeT(buffer[(int)(i-offset)]);
            long x = storage.write(i*getByteSizeOfT(), getByteSizeOfT(), b);
            if ( x != getByteSizeOfT() ) {
                throw new IllegalStateException("Could not write the requested number of bytes");
            }
        }
        return len;
    }

    abstract int getByteSizeOfT();
    abstract T decodeT(byte[] bytes);
    abstract byte[] encodeT(T t);
}
