package jb.filesystem.files.synchronization;

public interface FileLocksProvider {
    void acquireLock(int fileId);
    void releaseLock(int fileId);
}
