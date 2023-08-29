package jb.filesystem.storage;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileStorage implements ByteStorage {

    private final RandomAccessFile inner;

    public FileStorage(RandomAccessFile inner) {
        this.inner = inner;
    }

    @Override
    public int read(int offset, int len, byte[] buffer) {
        int bytesRead = getSize() - offset;
        if (bytesRead > len) { bytesRead = len; }

        try {
            inner.seek(offset);
            inner.read(buffer, 0, bytesRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesRead;
    }

    @Override
    public int write(int offset, int len, byte[] buffer) {
        int bytesToWrite = getSize() - offset;
        if (bytesToWrite > len) { bytesToWrite = len; }

        try {
            inner.seek(offset);
            inner.write(buffer, 0, bytesToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytesToWrite;
    }

    @Override
    public int getSize() {
        try {
            return (int) inner.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
