package jb.filesystem.blocks.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

/**
 * A metadata block, used as an entry-point to all the information about a file.
 * Contains the name of the file, current size and list of pointers to DataBlocks, where the file content is kept.
 * This list is of fixed size and in some cases (for large files) it is not enough. For that situation, this class
 * stores a list of pointers to other metadata blocks, which help locating the rest of the file. Those pointers are
 * referred to as indirect data blocks.
 */
public class FileMetadata implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.REGULAR;

    private String name;
    private final List<Integer> dataBlocks;
    private final List<Integer> indirectDataBlocks;
    private int fileSize;

    public FileMetadata(byte[] bytes) {
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        int fileType = decoder.readInteger(1);
        if (fileType != FILE_TYPE.id) {
            throw new IllegalArgumentException("The file type does not match");
        }
        this.name = decoder.readString(CONFIG.FILE_METADATA_NAME_SIZE);
        this.dataBlocks = decoder.readList(CONFIG.FILE_METADATA_MAX_COUNT_DATA_BLOCKS,
                CONFIG.DATA_BLOCK_POINTER_SIZE_BYTES);
        this.fileSize = decoder.readInteger(CONFIG.FILE_METADATA_FILE_SIZE_BYTES);
        this.indirectDataBlocks = decoder.readList(CONFIG.FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT,
                    CONFIG.METADATA_BLOCK_POINTER_SIZE_BYTES);
    }

    public FileMetadata(String name) {
        if (name.length() > CONFIG.FILE_METADATA_NAME_SIZE) {
            throw new IllegalArgumentException("Name too long");
        }
        this.name = name;
        this.dataBlocks = new ArrayList<>();
        this.indirectDataBlocks = new ArrayList<>();
    }

    @Override
    public byte[] toBytes() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(FILE_TYPE.id, 1);
        encoder.writeString(name, CONFIG.FILE_METADATA_NAME_SIZE);
        encoder.writeList(dataBlocks, CONFIG.FILE_METADATA_MAX_COUNT_DATA_BLOCKS,
                CONFIG.DATA_BLOCK_POINTER_SIZE_BYTES);
        encoder.writeInt(fileSize, CONFIG.FILE_METADATA_FILE_SIZE_BYTES);
        encoder.writeList(indirectDataBlocks, CONFIG.FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT,
                    CONFIG.METADATA_BLOCK_POINTER_SIZE_BYTES);
        return encoder.getResult();
    }

    @Override
    public FileType getType() {
        return FileType.REGULAR;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        if (newName.length() > CONFIG.FILE_METADATA_NAME_SIZE) {
            throw new IllegalArgumentException("Name too long");
        }
        name = newName;
    }

    public boolean areIndirectSlotsFull() {
        return indirectDataBlocks.size() >= CONFIG.FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT;
    }

    public void appendIndirectBlock(int blockId) {
        if (areIndirectSlotsFull()) {
            throw new IllegalStateException("The indirect block slots are full");
        }
        indirectDataBlocks.add(blockId);
    }

    public List<Integer> getIndirectDataBlocks() {
        return indirectDataBlocks;
    }

    public boolean hasDataBlocksSlotsAvailable() {
        return dataBlocks.size() < CONFIG.FILE_METADATA_MAX_COUNT_DATA_BLOCKS;
    }

    public List<Integer> getDataBlocks() {
        return dataBlocks;
    }

    public void addDataBlock(int dataBlockId) {
        if (dataBlocks.size() >= CONFIG.FILE_METADATA_MAX_COUNT_DATA_BLOCKS) {
            throw new IllegalStateException("Too many data blocks");
        }
        dataBlocks.add(dataBlockId);
    }

    public int getFileSize() {
        return fileSize;
    }

    public void maybeIncreaseFileSize(int fileSize) {
        this.fileSize = Math.max(fileSize, this.fileSize);
    }

    public void maybeReduceFileSize(int fileSize) {
        this.fileSize = Math.min(fileSize, this.fileSize);
    }

    public void removeLastDataBlock() {
        dataBlocks.remove(dataBlocks.size()-1);
    }

    public void removeLastIndirectDataBlock() {
        indirectDataBlocks.remove(indirectDataBlocks.size()-1);
    }
}
