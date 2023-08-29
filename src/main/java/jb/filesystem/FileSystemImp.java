package jb.filesystem;

import jb.filesystem.files.accessors.DirectoryAccessorI;
import jb.filesystem.files.accessors.FileAccessorI;
import jb.filesystem.files.FileFactory;
import jb.filesystem.files.FileI;
import jb.filesystem.utils.PathUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileSystemImp implements FileSystemI {

    private final FileAccessorI fileAccessor;
    private final DirectoryAccessorI directoryAccessor;
    private final FileFactory fileFactory;
    private final int rootDirectoryId;
    private final PathUtils pathUtils;

    public FileSystemImp(FileAccessorI fileAccessor, DirectoryAccessorI directoryAccessor,
                         FileFactory fileFactory, int rootDirId, PathUtils pathUtils) {
        this.fileAccessor = fileAccessor;
        this.directoryAccessor = directoryAccessor;
        this.fileFactory = fileFactory;
        this.rootDirectoryId = rootDirId;
        this.pathUtils = pathUtils;
    }

    @Override
    public FileI createFile(String pathToDir, String name) {
        // TODO: throw a checked exception
        if (get(pathUtils.concatenatePaths(pathToDir, name)).isPresent()) {
            throw new IllegalArgumentException("File with that name already exists");
        }
        int parentDirId = getDirectoryId(pathToDir);
        int blockId = fileAccessor.createFile(name);
        directoryAccessor.addFile(parentDirId, blockId);
        return wrapFile(blockId);
    }

    @Override
    public FileI createDirectory(String pathToParentDir, String name) {
        // TODO: throw a checked exception
        if (get(pathUtils.concatenatePaths(pathToParentDir, name)).isPresent()) {
            throw new IllegalArgumentException("File with that name already exists");
        }
        int parentDirId = getDirectoryId(pathToParentDir);
        int newDirId = directoryAccessor.createDirectory(name);
        directoryAccessor.addFile(parentDirId, newDirId);
        return wrapFile(newDirId);
    }

    @Override
    public Optional<FileI> get(String pathToFile) {
        String pathToDir = pathUtils.getDirectoryOf(pathToFile);
        String name = pathUtils.getFileName(pathToFile);

        int parentDirId = getDirectoryId(pathToDir);
        Optional<Integer> file = directoryAccessor.getFileId(parentDirId, name);
        return file.map(this::wrapFile);
    }

    @Override
    public boolean delete(String pathToFile) {
        String pathToDir = pathUtils.getDirectoryOf(pathToFile);
        String fileName = pathUtils.getFileName(pathToFile);
        int parentDirId = getDirectoryId(pathToDir);
        return get(pathUtils.concatenatePaths(pathToDir, fileName)).map(file -> {
            if (file.isRegularFile()) {
                return directoryAccessor.deleteRegularFile(parentDirId, fileName);
            } else {
                String dirToDeletePath = pathUtils.concatenatePaths(pathToDir, fileName);
                return deleteChildrenOf(dirToDeletePath) & directoryAccessor.deleteDirectory(parentDirId, fileName);
            }
        }).orElse(false);
    }

    private boolean deleteChildrenOf(String pathToDir) {
        List<FileI> children = listFiles(pathToDir);
        return children.stream()
                .map(child -> delete(pathUtils.concatenatePaths(pathToDir, child.getName())))
                .reduce((x,y) -> x&y)
                .orElse(true);
    }

    @Override
    public boolean rename(String pathToDir, String oldName, String newName) {
        Optional<FileI> f = get(pathUtils.concatenatePaths(pathToDir, oldName));
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
        List<String> absolutePath = pathUtils.splitAbsolutePath(path);
        for (String s : absolutePath) {
            Optional<Integer> file = directoryAccessor.getFileId(currentLocation, s);
            currentLocation = file.orElseThrow();
        }
        return currentLocation;
    }
}
