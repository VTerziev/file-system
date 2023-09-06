package jb.filesystem.storage;

/**
 * An implementation of ByteStorage, which only keeps the data in-memory. Used mainly for tests.
 */
public class InMemoryStorage implements ByteStorage {

    private final long byteSize;
    private final byte[] memory;

    public InMemoryStorage(int byteSize) {
        this.byteSize = byteSize;
        this.memory = new byte[byteSize];
    }

    @Override
    public long read(long offset, long len, byte[] buffer) {
        long bytesRead = byteSize - offset;
        if (bytesRead > len) { bytesRead = len; }

        for (long i = offset ; i < len+offset && i < byteSize ; i ++ ) {
            buffer[(int)(i-offset)] = memory[(int)i];
        }

        return bytesRead;
    }

    @Override
    public long write(long offset, long len, byte[] buffer) {
        long bytesToWrite = byteSize - offset;
        if (bytesToWrite > len) { bytesToWrite = len; }

        for (long i = offset ; i < offset+len && i < byteSize ; i ++ ) {
            memory[(int)i] = buffer[(int)(i-offset)];
        }

        return bytesToWrite;
    }

    @Override
    public long getSize() {
        return byteSize;
    }
}
