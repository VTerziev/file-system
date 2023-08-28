package jb.filesystem.files;

public interface FileI {

    // How many bytes were read
    int read(int offset, int len, byte[] buffer);

    // How many bytes were written
    int write(int offset, int len, byte[] buffer);
    String getName();
    int getFileId();
    boolean isRegularFile();
    boolean isDirectory();
    void rename(String newName);
}
