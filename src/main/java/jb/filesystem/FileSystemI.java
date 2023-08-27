package jb.filesystem;

import jb.filesystem.files.FileI;

import java.util.List;
import java.util.Optional;

public interface FileSystemI {
    FileI createFile(String pathToDir, String name);

    FileI createDirectory(String pathToParentDir, String name);

    Optional<FileI> get(String pathToDir, String name);

    // Returns if deleting the file was successful
    boolean delete(String pathToDir, String file);

    // Returns if renaming the file was successful
    boolean rename(String pathToDir, String oldName, String newName);

    List<FileI> listFiles(String dir);
}
