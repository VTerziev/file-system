package jb.filesystem.blocks.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

public class DataBlocksPointers implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.DATA_BLOCKS_POINTERS;
    private final List<Integer> dataBlocks;

    public DataBlocksPointers(byte[] bytes) {
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        int fileType = decoder.readInteger(1);
        if (fileType != FILE_TYPE.id) {
            throw new IllegalArgumentException("The file type does not match");
        }
        this.dataBlocks = decoder.readList(CONFIG.DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN,
                CONFIG.DATA_BLOCK_POINTER_SIZE_BYTES);
    }

    public DataBlocksPointers() {
        this.dataBlocks = new ArrayList<>();
    }

    @Override
    public byte[] toBytes() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(FILE_TYPE.id, 1);
        encoder.writeList(dataBlocks, CONFIG.DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN,
                CONFIG.DATA_BLOCK_POINTER_SIZE_BYTES);
        return encoder.getResult();
    }

    @Override
    public FileType getType() {
        return FILE_TYPE;
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean hasDataBlocksSlotsAvailable() {
        return dataBlocks.size() < CONFIG.DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN;
    }

    public List<Integer> getDataBlocks() {
        return dataBlocks;
    }

    public void addDataBlock(int blockId) {
        this.dataBlocks.add(blockId);
    }

    public void removeLastDataBlock() {
        this.dataBlocks.remove(this.dataBlocks.size()-1);
    }
}
