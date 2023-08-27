package jb.filesystem;

import jb.filesystem.accessors.DirectoryAccessor;
import jb.filesystem.accessors.FileAccessor;
import jb.filesystem.files.FileFactory;
import jb.filesystem.files.FileI;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemImp implements FileSystemI {

    private final FileAccessor fileAccessor;
    private final DirectoryAccessor directoryAccessor;
    private final FileFactory fileFactory; // TODO: rename
    private final int rootDirectoryId;

    public FileSystemImp(FileAccessor fileAccessor, DirectoryAccessor directoryAccessor,
                         FileFactory fileFactory, int rootDirId) {
        this.fileAccessor = fileAccessor;
        this.directoryAccessor = directoryAccessor;
        this.fileFactory = fileFactory;
        this.rootDirectoryId = rootDirId;
    }

    @Override
    public FileI createFile(String pathToDir, String name) {
        int parentDirId = getDirectory(pathToDir);
        int blockId = fileAccessor.createFile(name);
        directoryAccessor.addFile(parentDirId, blockId);
        return wrapFile(blockId);
    }

    @Override
    public FileI createDirectory(String pathToParentDir, String name) {
        int parentDirId = getDirectory(pathToParentDir);
        int newDirId = directoryAccessor.createDirectory(name);
        directoryAccessor.addFile(parentDirId, newDirId);
        return wrapFile(newDirId);
    }

    @Override
    public Optional<FileI> get(String pathToDir, String name) {
        int parentDirId = getDirectory(pathToDir);
        Optional<Integer> file = directoryAccessor.getFileId(parentDirId, name);
        return file.map(this::wrapFile);
    }

    @Override
    public boolean delete(String pathToDir, String fileName) {
        int parentDirId = getDirectory(pathToDir);
        return directoryAccessor.deleteFile(parentDirId, fileName);
    }

    @Override
    public boolean rename(String pathToDir, String oldName, String newName) {
        Optional<FileI> f = get(pathToDir, oldName);
        return f.map(file -> {
            file.rename(newName);
            return true;
        }).orElse(false);
    }

    @Override
    public List<FileI> listFiles(String dir) {
        int dirId = getDirectory(dir);
        return directoryAccessor.getAllFilesIn(dirId)
                .stream().map(this::wrapFile)
                .collect(Collectors.toList());
    }

    private FileI wrapFile(int fileId) {
        return fileFactory.wrapFile(fileId);
    }

    public int getDirectory(String path) {
        int currentLocation = rootDirectoryId;
        String pathSeparator = "/";
        List<String> absolutePath = Stream.of(path.split(pathSeparator))
                .filter(dir -> !dir.isBlank())
                .collect(Collectors.toList());
        for (String s : absolutePath) {
            Optional<Integer> file = directoryAccessor.getFileId(currentLocation, s);
            currentLocation = file.orElseThrow();
        }
        return currentLocation;
    }
}
