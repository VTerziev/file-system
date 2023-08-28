package jb.filesystem.files.accessors;

public interface FileAccessorI {
    String getName(int fileId);
    int writeToAFile(int fileId, int offset, byte[] buffer, int len);
    int readFromFile(int fileId, int offset, byte[] buffer, int len);
    int createFile(String name);
    boolean deleteFile(int fileId);
    void renameFile(int fileId, String newName);
    int getFileSize(int fileId);
}
