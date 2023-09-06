package jb.filesystem.storage;

public interface ByteStorage {

    /**
     * Read bytes from the storage
     * @param offset - the offset from the beginning of the storage, where to start reading
     * @param len - how many bytes to read
     * @param buffer - where to store the result
     * @return the number of bytes, which were successfully read
     */
    // TODO: maybe don't return anything
    long read(long offset, long len, byte[] buffer);

    /**
     * Write bytes to the storage
     * @param offset - the offset from the beginning of the storage, where to start writing
     * @param len - how many bytes to write
     * @param buffer - contains what should be written in the storage
     * @return the number of bytes, which were successfully written
     */
    // TODO: maybe don't return anything
    long write(long offset, long len, byte[] buffer);

    long getSize();
}
