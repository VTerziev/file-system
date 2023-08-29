package jb.filesystem.storage;

/**
 * An implementation of ByteStorage, which only keeps the data in-memory. Used mainly for tests.
 */
public class InMemoryStorage implements ByteStorage {

    private final int byteSize;
    private final byte[] memory;

    public InMemoryStorage(int byteSize) {
        this.byteSize = byteSize;
        this.memory = new byte[byteSize];
    }

    @Override
    public int read(int offset, int len, byte[] buffer) {
        int bytesRead = byteSize - offset;
        if (bytesRead > len) { bytesRead = len; }

        for (int i = offset ; i < len+offset && i < byteSize ; i ++ ) {
            buffer[i-offset] = memory[i];
        }

        return bytesRead;
    }

    @Override
    public int write(int offset, int len, byte[] buffer) {
        int bytesToWrite = byteSize - offset;
        if (bytesToWrite > len) { bytesToWrite = len; }

        for (int i = offset ; i < offset+len && i < byteSize ; i ++ ) {
            memory[i] = buffer[i-offset];
        }

        return bytesToWrite;
    }

    @Override
    public int getSize() {
        return byteSize;
    }
}
