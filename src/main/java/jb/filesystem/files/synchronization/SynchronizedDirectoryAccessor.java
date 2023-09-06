package jb.filesystem.files.synchronization;

import jb.filesystem.files.accessors.DirectoryAccessorI;

import java.util.List;
import java.util.Optional;

public class SynchronizedDirectoryAccessor implements DirectoryAccessorI {

    private final DirectoryAccessorI inner;
    private final FileLocksProvider locksProvider;

    public SynchronizedDirectoryAccessor(DirectoryAccessorI inner, FileLocksProvider locksProvider) {
        this.inner = inner;
        this.locksProvider = locksProvider;
    }

    @Override
    public String getName(int directoryId) {
        locksProvider.acquireLock(directoryId);
        String result = inner.getName(directoryId);
        locksProvider.releaseLock(directoryId);
        return result;
    }

    @Override
    public Optional<Integer> getFileId(int directoryId, String fileName) {
        locksProvider.acquireLock(directoryId);
        Optional<Integer> result = inner.getFileId(directoryId, fileName);
        locksProvider.releaseLock(directoryId);
        return result;
    }

    @Override
    public List<Integer> getAllFilesIn(int directoryId) {
        locksProvider.acquireLock(directoryId);
        List<Integer> result = inner.getAllFilesIn(directoryId);
        locksProvider.releaseLock(directoryId);
        return result;
    }

    @Override
    public void addFile(int directoryId, int fileId) {
        locksProvider.acquireLock(directoryId);
        inner.addFile(directoryId, fileId);
        locksProvider.releaseLock(directoryId);
    }

    @Override
    public int createDirectory(String fileName) {
        return inner.createDirectory(fileName);
    }

    @Override
    public void renameDirectory(int directoryId, String newName) {
        locksProvider.acquireLock(directoryId);
        inner.renameDirectory(directoryId, newName);
        locksProvider.releaseLock(directoryId);
    }

    @Override
    public boolean deleteRegularFile(int directoryId, String fileName) {
        locksProvider.acquireLock(directoryId);
        boolean result = inner.deleteRegularFile(directoryId, fileName);
        locksProvider.releaseLock(directoryId);
        return result;
    }

    @Override
    public boolean deleteDirectory(int parentDirId, String directoryToDelete) {
        locksProvider.acquireLock(parentDirId);
        boolean result = inner.deleteDirectory(parentDirId, directoryToDelete);
        locksProvider.releaseLock(parentDirId);
        return result;
    }
}
