package jb.filesystem.files;// TODO: package

public interface FileI {

    // How many bytes were read
    int read(int offset, int len, byte[] buffer);

    // How many bytes were written
    int write(int offset, int len, byte[] buffer);

    int getFileId();
    boolean isRegularFile();
    boolean isDirectory();
    void rename(String newName);
}
