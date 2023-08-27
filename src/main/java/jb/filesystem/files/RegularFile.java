package jb.filesystem.files;

import jb.filesystem.accessors.FileAccessor;

public class RegularFile implements FileI {

    private final int fileId;
    private final FileAccessor fileAccessor;

    public RegularFile(int fileId, FileAccessor fileAccessor) {
        this.fileId = fileId;
        this.fileAccessor = fileAccessor;
    }

    @Override
    public int read(int offset, int len, byte[] buffer) {
        return fileAccessor.readFromFile(fileId, offset, len, buffer);
    }

    @Override
    public int write(int offset, int len, byte[] buffer) {
        return fileAccessor.writeToAFile(fileId, offset, buffer, len);
    }

    @Override
    public int getFileId() {
        return fileId;
    }

    @Override
    public boolean isRegularFile() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void rename(String newName) {
        fileAccessor.renameFile(fileId, newName);
    }
}