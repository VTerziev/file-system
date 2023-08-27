package jb.filesystem.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.data.DataBlock.DATA_BLOCK_POINTER_SIZE_BYTES;

public class FileMetadata implements MetadataBlock {
    private static final int NAME_SIZE = 5;
    public static final int MAX_COUNT_DATA_BLOCKS = 2;
    private static final int MAX_INDIRECT_COUNT_DATA_BLOCKS = 2;
    private static final int FILE_SIZE_BYTES = 2;
    private static final FileType FILE_TYPE = FileType.REGULAR;

    private String name;
    private final List<Integer> dataBlocks;
    private final List<Integer> indirectDataBlocks;
    private int fileSize;

    public FileMetadata(byte[] bytes) {
        // TODO: assert the byte size? use preconditions?
        if (bytes.length != MetadataBlock.BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException("Incorrect number of bytes");
        }

        // TODO: refactor this somehow
        if (MetadataBlock.BLOCK_SIZE_BYTES != 1 + NAME_SIZE +
                    DATA_BLOCK_POINTER_SIZE_BYTES*MAX_COUNT_DATA_BLOCKS +
                    FILE_SIZE_BYTES +
                    2*METADATA_BLOCK_POINTER_SIZE_BYTES // 2 levels of indirection
        ) {
            throw new IllegalArgumentException("Incorrect number of bytes");
        }
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        decoder.readInteger(1); // file type  TODO: maybe assert that it is correct?
        this.name = decoder.readString(NAME_SIZE);
        this.dataBlocks = decoder.readList(MAX_COUNT_DATA_BLOCKS, DATA_BLOCK_POINTER_SIZE_BYTES);
        this.fileSize = decoder.readInteger(FILE_SIZE_BYTES);
        this.indirectDataBlocks = decoder.readList(MAX_INDIRECT_COUNT_DATA_BLOCKS, METADATA_BLOCK_POINTER_SIZE_BYTES);
    }

    public FileMetadata(String name) {
        if (name.length() > NAME_SIZE) {
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
        encoder.writeString(name, NAME_SIZE);
        encoder.writeList(dataBlocks, MAX_COUNT_DATA_BLOCKS, DATA_BLOCK_POINTER_SIZE_BYTES);
        encoder.writeInt(fileSize, FILE_SIZE_BYTES);
        encoder.writeList(indirectDataBlocks, MAX_INDIRECT_COUNT_DATA_BLOCKS, METADATA_BLOCK_POINTER_SIZE_BYTES);
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
        if (newName.length() > NAME_SIZE) {
            throw new IllegalArgumentException("Name too long");
        }
        name = newName;
    }

    public boolean areIndirectSlotsFull() {
        return indirectDataBlocks.size() >= MAX_INDIRECT_COUNT_DATA_BLOCKS;
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
        return dataBlocks.size() < MAX_COUNT_DATA_BLOCKS;
    }

    public List<Integer> getDataBlocks() {
        return dataBlocks;
    }

    public void addDataBlock(int dataBlockId) {
        dataBlocks.add(dataBlockId); // TODO: safety checks
    }

    public int getFileSize() {
        return fileSize;
    }

    public void maybeIncreaseFileSize(int fileSize) {
        this.fileSize = Math.max(fileSize, this.fileSize);
    }

    public void removeLastDataBlock() {
        dataBlocks.remove(dataBlocks.size()-1);
    }

    public void removeLastIndirectDataBlock() {
        indirectDataBlocks.remove(indirectDataBlocks.size()-1);
    }
}
