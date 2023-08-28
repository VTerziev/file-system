package jb.filesystem.files;

import jb.filesystem.files.accessors.DirectoryAccessorI;

public class DirectoryFile implements FileI { // TODO
    private final int fileId;
    private final DirectoryAccessorI directoryAccessor;

    public DirectoryFile(int fileId, DirectoryAccessorI directoryAccessor) {
        this.fileId = fileId;
        this.directoryAccessor = directoryAccessor;
    }

    @Override
    public int read(int offset, int len, byte[] buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int write(int offset, int len, byte[] buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return directoryAccessor.getName(fileId);
    }

    @Override
    public int getFileId() {
        return fileId;
    }

    @Override
    public boolean isRegularFile() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public void rename(String newName) {
        directoryAccessor.renameDirectory(fileId, newName);
    }
}
