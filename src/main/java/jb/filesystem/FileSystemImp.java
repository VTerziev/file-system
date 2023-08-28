package jb.filesystem;

import jb.filesystem.files.accessors.DirectoryAccessorI;
import jb.filesystem.files.accessors.FileAccessorI;
import jb.filesystem.files.FileFactory;
import jb.filesystem.files.FileI;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemImp implements FileSystemI {

    private final FileAccessorI fileAccessor;
    private final DirectoryAccessorI directoryAccessor;
    private final FileFactory fileFactory; // TODO: rename
    private final int rootDirectoryId;

    public FileSystemImp(FileAccessorI fileAccessor, DirectoryAccessorI directoryAccessor,
                         FileFactory fileFactory, int rootDirId) {
        this.fileAccessor = fileAccessor;
        this.directoryAccessor = directoryAccessor;
        this.fileFactory = fileFactory;
        this.rootDirectoryId = rootDirId;
    }

    @Override
    public FileI createFile(String pathToDir, String name) {
        int parentDirId = getDirectoryId(pathToDir);
        int blockId = fileAccessor.createFile(name);
        directoryAccessor.addFile(parentDirId, blockId);
        return wrapFile(blockId);
    }

    @Override
    public FileI createDirectory(String pathToParentDir, String name) {
        int parentDirId = getDirectoryId(pathToParentDir);
        int newDirId = directoryAccessor.createDirectory(name);
        directoryAccessor.addFile(parentDirId, newDirId);
        return wrapFile(newDirId);
    }

    @Override
    public Optional<FileI> get(String pathToDir, String name) {
        int parentDirId = getDirectoryId(pathToDir);
        Optional<Integer> file = directoryAccessor.getFileId(parentDirId, name);
        return file.map(this::wrapFile);
    }

    @Override
    public boolean delete(String pathToDir, String fileName) {
        int parentDirId = getDirectoryId(pathToDir);
        return get(pathToDir, fileName).map(file -> {
            if (file.isRegularFile()) {
                return directoryAccessor.deleteRegularFile(parentDirId, fileName);
            } else {
                String dirToDeletePath = pathToDir + "/" + fileName; // TODO: refactor
                return deleteChildrenOf(dirToDeletePath) & directoryAccessor.deleteDirectory(parentDirId, fileName);
            }
        }).orElse(false);
    }

    private boolean deleteChildrenOf(String pathToDir) {
        List<FileI> children = listFiles(pathToDir);
        return children.stream()
                .map(child -> delete(pathToDir, child.getName()))
                .reduce((x,y) -> x&y)
                .orElse(true);
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
        int dirId = getDirectoryId(dir);
        return directoryAccessor.getAllFilesIn(dirId)
                .stream().map(this::wrapFile)
                .collect(Collectors.toList());
    }

    private FileI wrapFile(int fileId) {
        return fileFactory.wrapFile(fileId);
    }

    private int getDirectoryId(String path) {
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
