package jb.filesystem.accessors;

import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.metadata.DirectoryMetadata;
import jb.filesystem.metadata.FileType;
import jb.filesystem.metadata.MetadataBlock;

import java.util.List;
import java.util.Optional;

public class DirectoryAccessor { // TODO: make concurrent
    private final MetadataBlocksManager metadataManager;
    private final FileAccessor fileAccessor;

    public DirectoryAccessor(MetadataBlocksManager metadataManager, FileAccessor fileAccessor) {
        this.metadataManager = metadataManager;
        this.fileAccessor = fileAccessor;
    }

    public void addFile(int directoryId, int fileId) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(directoryId);
        metadata.addChild(fileId);
        metadataManager.saveBlock(directoryId, metadata);
    }

    public Optional<Integer> getFileId(int directoryId, String fileName) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(directoryId);
        return metadata.getChildren()
                .stream()
                .filter(x -> metadataManager.getBlock(x).getName().equals(fileName))
                .findAny();
    }

    public boolean deleteFile(int directoryId, String fileName) {
        Optional<Integer> fileIdOptional = getFileId(directoryId, fileName);
        return fileIdOptional.map(id -> deleteFileById(directoryId, id)).orElse(false);
    }

    public boolean deleteFilesInDir(int directoryId) {
        return getAllFilesIn(directoryId)
                .stream()
                .map(fileId -> deleteFileById(directoryId, fileId))
                .reduce((x,y) -> x|y).orElse(true);

    }

    private boolean deleteFileById(int directoryId, int fileId) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(directoryId);
        metadata.deleteChild(fileId);
        metadataManager.saveBlock(directoryId, metadata);

        MetadataBlock fileMetadata = metadataManager.getBlock(fileId);
        if (fileMetadata.getType() == FileType.DIRECTORY) { // TODO: refactor
            return deleteFilesInDir(fileId);
        } else if (fileMetadata.getType() == FileType.REGULAR){
            fileAccessor.deleteFile(fileId);
            return true;
        }
        throw new IllegalStateException("File type not found");
    }

    public List<Integer> getAllFilesIn(int directoryId) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(directoryId);
        return metadata.getChildren();
    }

    public int createDirectory(String fileName) {
        int blockId = metadataManager.allocateBlock();
        DirectoryMetadata metadata = new DirectoryMetadata(fileName);
        metadataManager.saveBlock(blockId, metadata);
        return blockId;
    }

    public void renameDirectory(int directoryId, String newName) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(directoryId);
        metadata.setName(newName);
        metadataManager.saveBlock(directoryId, metadata);
    }

    public String getName(int directoryId) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(directoryId);
        return metadata.getName();
    }
}
