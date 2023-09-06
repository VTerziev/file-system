package jb.filesystem.files;

/**
 * Represents a file. Could be either a directory or a regular file.
 */
public interface FileI {

    /**
     * Read some bytes from the file
     * @param offset - offset from the beginning of the file, where to start reading
     * @param len - how many bytes to read
     * @param buffer - where to store the result
     * @return returns the number of successfully read bytes
     */
    int read(int offset, int len, byte[] buffer);

    /**
     * Write some bytes from the file
     * @param offset - offset from the beginning of the file, where to start writing
     * @param len - how many bytes to write
     * @param buffer - what to write in the file
     * @return returns the number of successfully written bytes
     */
    int write(int offset, int len, byte[] buffer);
    String getName();
    int getFileId();

    /**
     * @return if the file is a regular file. The alternative would be a directory
     */
    boolean isRegularFile();

    /**
     * @return if the file is a directory. The alternative would be a regular file
     */
    boolean isDirectory();
    void rename(String newName);
    int getSize();
}
