package jb.filesystem.storage;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * An implementation of ByteStorage, which internally uses a file to store the data.
 */
public class FileStorage implements ByteStorage {

    private final RandomAccessFile inner;

    public FileStorage(RandomAccessFile inner) {
        this.inner = inner;
    }

    @Override
    public long read(long offset, long len, byte[] buffer) {
        long bytesRead = getSize() - offset;
        if (bytesRead > len) { bytesRead = len; }

        try {
            inner.seek(offset);
            inner.read(buffer, 0, (int)bytesRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesRead;
    }

    @Override
    public long write(long offset, long len, byte[] buffer) {
        long bytesToWrite = getSize() - offset;
        if (bytesToWrite > len) { bytesToWrite = len; }

        try {
            inner.seek(offset);
            inner.write(buffer, 0, (int)bytesToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesToWrite;
    }

    @Override
    public long getSize() {
        try {
            return (int) inner.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
