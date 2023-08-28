package jb.filesystem.files.accessors;

import java.util.List;
import java.util.Optional;

public interface DirectoryAccessorI {
    String getName(int directoryId);
    Optional<Integer> getFileId(int directoryId, String fileName);
    List<Integer> getAllFilesIn(int directoryId);
    void addFile(int directoryId, int fileId);
    int createDirectory(String fileName);
    void renameDirectory(int directoryId, String newName);
    boolean deleteRegularFile(int directoryId, String fileName);
    boolean deleteDirectory(int parentDirId, String directoryToDelete);
}
