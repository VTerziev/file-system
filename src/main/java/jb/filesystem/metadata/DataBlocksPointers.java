package jb.filesystem.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.data.DataBlock.DATA_BLOCK_POINTER_SIZE_BYTES;

public class DataBlocksPointers implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.DATA_BLOCKS_POINTERS;
    private final List<Integer> dataBlocks;
    public static final int MAX_COUNT_DATA_BLOCKS = 7;

    public DataBlocksPointers(byte[] bytes) {
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        decoder.readInteger(1); // file type  TODO: maybe assert that it is correct?
        this.dataBlocks = decoder.readList(MAX_COUNT_DATA_BLOCKS, DATA_BLOCK_POINTER_SIZE_BYTES);
    }

    public DataBlocksPointers() {
        this.dataBlocks = new ArrayList<>();
    }

    @Override
    public byte[] toBytes() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(FILE_TYPE.id, 1);
        encoder.writeList(dataBlocks, MAX_COUNT_DATA_BLOCKS, DATA_BLOCK_POINTER_SIZE_BYTES);
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
        return dataBlocks.size() < MAX_COUNT_DATA_BLOCKS;
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
