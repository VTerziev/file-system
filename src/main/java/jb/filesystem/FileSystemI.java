package jb.filesystem;

import jb.filesystem.files.FileI;

import java.util.List;
import java.util.Optional;

public interface FileSystemI {
    /**
     * Create a regular file inside a given directory
     * @param pathToDir - absolute path to the directory
     * @param name - name of the new file
     * @return an object, corresponding to the newly created file
     */
    FileI createFile(String pathToDir, String name);

    /**
     * Create a directory inside a given parent directory
     * @param pathToParentDir - absolute path to the parent directory
     * @param name - name of the new directory
     * @return an object, corresponding to the newly created directory
     */
    FileI createDirectory(String pathToParentDir, String name);

    /**
     * Get an object, corresponding to a file
     * @param pathToFile - absolute path to the file
     * @return Optional, which is empty if the file is not found
     */
    Optional<FileI> get(String pathToFile);

    /**
     * Delete a file
     * @return true if the file existed and was deleted
     */
    boolean delete(String pathToFile);

    /**
     * Rename a file
     * @param pathToDir - absolute path to the directory, containing the file
     * @param oldName - the old name of the file
     * @param newName - the new name of the file
     * @return true if the file existed and was renamed
     */
    boolean rename(String pathToDir, String oldName, String newName);

    /**
     * Get the list of files, contained in a directory
      * @param dir - absolute path to the directory
     */
    List<FileI> listFiles(String dir);
}
