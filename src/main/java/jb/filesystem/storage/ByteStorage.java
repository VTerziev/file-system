package jb.filesystem.storage;

public interface ByteStorage {
    // How many bytes were read
    int read(int offset, int len, byte[] buffer);

    // How many bytes were written
    int write(int offset, int len, byte[] buffer);

    int getSize();
}
