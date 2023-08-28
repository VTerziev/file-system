package jb.filesystem.accessors;

public interface FileLocksProvider { // Move to another package
    void acquireLock(int fileId);
    void releaseLock(int fileId);
}
