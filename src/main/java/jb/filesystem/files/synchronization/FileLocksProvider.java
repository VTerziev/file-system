package jb.filesystem.files.synchronization;

public interface FileLocksProvider { // Move to another package
    void acquireLock(int fileId);
    void releaseLock(int fileId);
}
