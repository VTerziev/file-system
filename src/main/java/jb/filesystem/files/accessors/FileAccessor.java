package jb.filesystem.files.accessors;

import jb.filesystem.blocks.blockmanager.DataBlocksManager;
import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.data.DataBlock;
import jb.filesystem.blocks.metadata.FileMetadata;
import jb.filesystem.blocks.traversing.RegularFileTraversable;
import jb.filesystem.blocks.traversing.Traversable;
import jb.filesystem.blocks.traversing.Traversor;
import jb.filesystem.utils.SplitByBlocks;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

public class FileAccessor implements FileAccessorI {

    private final MetadataBlocksManager metadataManager;
    private final DataBlocksManager dataBlocksManager;
    private final Traversor traversor;

    public FileAccessor(MetadataBlocksManager metadataManager,
                        DataBlocksManager dataBlocksManager,
                        Traversor traversor) {
        this.metadataManager = metadataManager;
        this.dataBlocksManager = dataBlocksManager;
        this.traversor = traversor;
    }

    public String getName(int fileId) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        return metadata.getName();
    }

    public int writeToAFile(int fileId, int offset, byte[] buffer, int len) {
        if (offset > getFileSize(fileId)) {
            throw new IllegalArgumentException("Offset out of file");
        }

        extendFile(fileId, offset+len);
        SplitByBlocks split = new SplitByBlocks(CONFIG.DATA_BLOCK_SIZE_BYTES, offset, offset+len);

        while (split.nextBlock()) {
            int currentBlockId = split.getCurrentBlockId();
            int startByteOfBlock = split.getCurrentBlockStart();
            int endByteOfBlock = split.getCurrentBlockEnd();

            int blockIdToWriteTo = getBlockId(fileId, currentBlockId);
            DataBlock blockToWriteTo = dataBlocksManager.getBlock(blockIdToWriteTo);
            blockToWriteTo.write(startByteOfBlock%CONFIG.DATA_BLOCK_SIZE_BYTES,
                    startByteOfBlock-offset, endByteOfBlock-offset, buffer);

            maybeIncreaseFileSize(fileId, endByteOfBlock);
            dataBlocksManager.saveBlock(blockIdToWriteTo, blockToWriteTo);
        }
        return len;
    }

    public int readFromFile(int fileId, int offset, byte[] buffer, int len) {
        if (offset+len > getFileSize(fileId)) {
            throw new IllegalArgumentException("Offset out of file");
        }

        SplitByBlocks split = new SplitByBlocks(CONFIG.DATA_BLOCK_SIZE_BYTES, offset, offset+len);
        while (split.nextBlock()) {
            int currentBlockId = split.getCurrentBlockId();
            int startByteOfBlock = split.getCurrentBlockStart();
            int endByteOfBlock = split.getCurrentBlockEnd();

            int blockIdToReadFrom = getBlockId(fileId, currentBlockId);
            DataBlock readBlock = dataBlocksManager.getBlock(blockIdToReadFrom);
            readBlock.read(startByteOfBlock%CONFIG.DATA_BLOCK_SIZE_BYTES, startByteOfBlock-offset, endByteOfBlock-offset, buffer);
        }
        return len;
    }

    public int createFile(String name) {
        int blockId = metadataManager.allocateBlock();
        FileMetadata metadata = new FileMetadata(name);
        metadataManager.saveBlock(blockId, metadata);
        return blockId;
    }

    public boolean deleteFile(int fileId) {
        shrinkFile(fileId, 0);
        metadataManager.deallocateBlock(fileId);
        return true;
    }

    public void renameFile(int fileId, String newName) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        metadata.setName(newName);
        metadataManager.saveBlock(fileId, metadata);
    }

    private int getBlockId(int fileId, int blockOffset) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        Traversable traversable = new RegularFileTraversable(metadata, fileId, metadataManager);
        return traversor.getLeaf(traversable, blockOffset);
    }

    private void extendFile(int fileId, int requiredBytes) {
        while (getAllocatedFileSize(fileId) < requiredBytes) {
            addADataBlockToAFile(fileId);
        }
    }

    private void shrinkFile(int fileId, int targetBytes) {
        while (getAllocatedFileSize(fileId) >= targetBytes + CONFIG.DATA_BLOCK_SIZE_BYTES) {
            deleteLastBlock(fileId);
        }
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        metadata.maybeReduceFileSize(targetBytes);
    }

    public int getFileSize(int fileId) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        return metadata.getFileSize();
    }

    private void maybeIncreaseFileSize(int fileId, int newFileSize) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        metadata.maybeIncreaseFileSize(newFileSize);
        metadataManager.saveBlock(fileId, metadata);
    }

    private int getAllocatedBlocksCount(int fileId) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        Traversable traversable = new RegularFileTraversable(metadata, fileId, metadataManager);
        return traversor.getTotalLeavesCount(traversable);
    }

    private void deleteLastBlock(int fileId) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        Traversable traversable = new RegularFileTraversable(metadata, fileId, metadataManager);
        int lastDataBlockId = traversor.getLeaf(traversable, getAllocatedBlocksCount(fileId)-1);
        traversor.deleteLastLeaf(traversable);
        dataBlocksManager.deallocateBlock(lastDataBlockId);

        metadata = metadataManager.getFileMetadata(fileId);
        metadata.maybeReduceFileSize(getAllocatedFileSize(fileId));
        metadataManager.saveBlock(fileId, metadata);
    }

    private int getAllocatedFileSize(int fileId) {
        return getAllocatedBlocksCount(fileId)*CONFIG.DATA_BLOCK_SIZE_BYTES;
    }

    private void addADataBlockToAFile(int fileId) {
        FileMetadata metadata = metadataManager.getFileMetadata(fileId);
        Traversable traversable = new RegularFileTraversable(metadata, fileId, metadataManager);
        int blockId = dataBlocksManager.allocateBlock();
        traversor.appendLeaf(traversable, blockId);
        metadataManager.saveBlock(fileId, metadata);
    }
}
