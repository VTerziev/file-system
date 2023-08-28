package jb.filesystem.files.synchronization;

import jb.filesystem.files.accessors.FileAccessorI;

public class SynchronizedFileAccessor implements FileAccessorI {

    private final FileAccessorI internal;
    private final FileLocksProvider locksProvider;

    public SynchronizedFileAccessor(FileAccessorI internal, FileLocksProvider locksProvider) {
        this.internal = internal;
        this.locksProvider = locksProvider;
    }

    @Override
    public String getName(int fileId) {
        locksProvider.acquireLock(fileId);
        String result = internal.getName(fileId);
        locksProvider.releaseLock(fileId);
        return result;
    }

    @Override
    public int writeToAFile(int fileId, int offset, byte[] buffer, int len) {
        locksProvider.acquireLock(fileId);
        int result = internal.writeToAFile(fileId, offset, buffer, len);
        locksProvider.releaseLock(fileId);
        return result;
    }

    @Override
    public int readFromFile(int fileId, int offset, byte[] buffer, int len) {
        locksProvider.acquireLock(fileId);
        int result = internal.readFromFile(fileId, offset, buffer, len);
        locksProvider.releaseLock(fileId);
        return result;
    }

    @Override
    public int createFile(String name) {
        return internal.createFile(name);
    }

    @Override
    public boolean deleteFile(int fileId) {
        locksProvider.acquireLock(fileId);
        boolean result = internal.deleteFile(fileId);
        locksProvider.releaseLock(fileId);
        return result;
    }

    @Override
    public void renameFile(int fileId, String newName) {
        locksProvider.acquireLock(fileId);
        internal.renameFile(fileId, newName);
        locksProvider.releaseLock(fileId);
    }

    @Override
    public int getFileSize(int fileId) {
        locksProvider.acquireLock(fileId);
        int result = internal.getFileSize(fileId);
        locksProvider.releaseLock(fileId);
        return result;
    }
}
