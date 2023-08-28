package jb.filesystem.files.accessors;

import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.metadata.DirectoryMetadata;

import java.util.List;
import java.util.Optional;

public class DirectoryAccessor implements DirectoryAccessorI {
    private final MetadataBlocksManager metadataManager;
    private final FileAccessorI fileAccessor;

    public DirectoryAccessor(MetadataBlocksManager metadataManager, FileAccessorI fileAccessor) {
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

    public boolean deleteRegularFile(int directoryId, String fileName) {
        Optional<Integer> fileIdOptional = getFileId(directoryId, fileName);

        return fileIdOptional.map(fileId -> {
            removeChildFromDir(directoryId, fileId);
            return fileAccessor.deleteFile(fileId);
        }).orElse(false);
    }

    public boolean deleteDirectory(int parentDirId, String directoryToDelete) {
        Optional<Integer> fileIdOptional = getFileId(parentDirId, directoryToDelete);

        return fileIdOptional.map(fileId -> {
            removeChildFromDir(parentDirId, fileId);
            assertDirEmpty(fileId);
            metadataManager.deallocateBlock(fileId);
            return true;
        }).orElse(false);
    }

    private void removeChildFromDir(int dirId, int childId) {
        DirectoryMetadata metadata = metadataManager.getDirectoryMetadata(dirId);
        metadata.deleteChild(childId);
        metadataManager.saveBlock(dirId, metadata);
    }

    private void assertDirEmpty(int dirId) {
        DirectoryMetadata dirToDeleteMetadata = metadataManager.getDirectoryMetadata(dirId);
        if (!dirToDeleteMetadata.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a non-empty directory");
        }
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
