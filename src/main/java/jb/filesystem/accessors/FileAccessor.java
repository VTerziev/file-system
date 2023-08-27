package jb.filesystem.accessors;

import jb.filesystem.blockmanager.DataBlocksManager;
import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.data.DataBlock;
import jb.filesystem.metadata.FileMetadata;
import jb.filesystem.traversing.Traversable;
import jb.filesystem.traversing.TraversablesFactory;
import jb.filesystem.traversing.Traversor;
import jb.filesystem.utils.SplitByBlocks;

public class FileAccessor { // TODO: make concurrent

    private final MetadataBlocksManager metadataManager;
    private final DataBlocksManager dataBlocksManager;
    private final TraversablesFactory factory;
    private final Traversor traversor;

    public FileAccessor(MetadataBlocksManager metadataManager, DataBlocksManager dataBlocksManager,
                        TraversablesFactory factory, Traversor traversor) {
        this.metadataManager = metadataManager;
        this.dataBlocksManager = dataBlocksManager;
        this.factory = factory;
        this.traversor = traversor;
    }

    public int writeToAFile(int fileId, int offset, byte[] buffer, int len) { // TODO: refactor
        if (offset > getFileSize(fileId)) { // TODO: should I thrown an exception in this case?
            throw new IllegalArgumentException("Offset out of file");
        }

        extendFile(fileId, offset+len);
        SplitByBlocks split = new SplitByBlocks(DataBlock.BLOCK_SIZE_BYTES, offset, offset+len);

        while (split.nextBlock()) {
            int currentBlockId = split.getCurrentBlockId();
            int startByteOfBlock = split.getCurrentBlockStart();
            int endByteOfBlock = split.getCurrentBlockEnd();

            int blockIdToWriteTo = getBlockId(fileId, currentBlockId);
            DataBlock blockToWriteTo = dataBlocksManager.getBlock(blockIdToWriteTo);
            blockToWriteTo.write(startByteOfBlock%DataBlock.BLOCK_SIZE_BYTES,
                    startByteOfBlock-offset, endByteOfBlock-offset, buffer);

            maybeIncreaseFileSize(fileId, endByteOfBlock);
            dataBlocksManager.saveBlock(blockIdToWriteTo, blockToWriteTo);
        }
        return 0; // TODO: fix
    }

    public int readFromFile(int fileId, int offset, int len, byte[] buffer) { // TODO: refactor arguments
        if (offset+len > getFileSize(fileId)) { // TODO: should I thrown an exception in this case?
            throw new IllegalArgumentException("Offset out of file");
        }

        SplitByBlocks split = new SplitByBlocks(DataBlock.BLOCK_SIZE_BYTES, offset, offset+len);
        while (split.nextBlock()) {
            int currentBlockId = split.getCurrentBlockId();
            int startByteOfBlock = split.getCurrentBlockStart();
            int endByteOfBlock = split.getCurrentBlockEnd();

            int blockIdToReadFrom = getBlockId(fileId, currentBlockId);
            DataBlock readBlock = dataBlocksManager.getBlock(blockIdToReadFrom);
            readBlock.read(startByteOfBlock%DataBlock.BLOCK_SIZE_BYTES, startByteOfBlock-offset, endByteOfBlock-offset, buffer);
        }
        return 0; // TODO: fix
    }

    public int createFile(String name) {
        int blockId = metadataManager.allocateBlock();
        FileMetadata metadata = new FileMetadata(name);
        metadataManager.saveBlock(blockId, metadata);
        return blockId;
    }

    public void deleteFile(int fileId) { // TODO: should be called only from a directory
        while (getAllocatedBlocksCount(fileId) > 0) {
            deleteLastBlock(fileId);
        }
        metadataManager.deallocateBlock(fileId);
    }

    public void renameFile(int fileId, String newName) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        metadata.setName(newName);
        metadataManager.saveBlock(fileId, metadata);
    }

    private int getBlockId(int fileId, int blockOffset) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        Traversable traversable = factory.buildTraversable(metadata, fileId);
        return traversor.getLeaf(traversable, blockOffset);
    }

    private void extendFile(int fileId, int requiredBytes) {
        while (getAllocatedFileSize(fileId) < requiredBytes) {
            // TODO: this happens twice the first time, because blockId 0 is interpreted as empty
            addADataBlockToAFile(fileId);
        }
    }

    public int getFileSize(int fileId) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        return metadata.getFileSize();
    }

    private void maybeIncreaseFileSize(int fileId, int newFileSize) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        metadata.maybeIncreaseFileSize(newFileSize);
        metadataManager.saveBlock(fileId, metadata);
    }

    private int getAllocatedBlocksCount(int fileId) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        Traversable traversable = factory.buildTraversable(metadata, fileId);
        return traversor.getTotalLeavesCount(traversable);
    }

    private void deleteLastBlock(int fileId) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        Traversable traversable = factory.buildTraversable(metadata, fileId);
        int lastDataBlockId = traversor.getLeaf(traversable, getAllocatedBlocksCount(fileId)-1);
        traversor.deleteLastLeaf(traversable);
        dataBlocksManager.deallocateBlock(lastDataBlockId);
    }

    private int getAllocatedFileSize(int fileId) {
        return getAllocatedBlocksCount(fileId)*DataBlock.BLOCK_SIZE_BYTES;
    }

    private void addADataBlockToAFile(int fileId) {
        FileMetadata metadata = (FileMetadata) metadataManager.getBlock(fileId);
        Traversable traversable = factory.buildTraversable(metadata, fileId);
        int blockId = dataBlocksManager.allocateBlock();
        traversor.appendLeaf(traversable, blockId);
        metadataManager.saveBlock(fileId, metadata);
    }
}
