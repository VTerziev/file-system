package jb.filesystem.files.accessors;

/**
 * A class which is used to facilitate all modifications to regular files.
 * A file consists of many blocks (metadata and data blocks) and they should be linked properly. This class takes care
 * of all the synchronization of different blocks.
 */
public interface FileAccessorI {
    String getName(int fileId);
    int writeToAFile(int fileId, int offset, byte[] buffer, int len);
    int readFromFile(int fileId, int offset, byte[] buffer, int len);
    int createFile(String name);
    boolean deleteFile(int fileId);
    void renameFile(int fileId, String newName);
    int getFileSize(int fileId);
}
