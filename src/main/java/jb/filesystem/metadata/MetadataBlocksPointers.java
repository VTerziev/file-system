package jb.filesystem.metadata;

import java.util.ArrayList;
import java.util.List;

public class MetadataBlocksPointers implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.METADATA_BLOCKS_POINTERS;
    private final List<Integer> metadataBlocks;
    private final int maxDepth;
    public static final int MAX_COUNT_DATA_BLOCKS = 7;

    public MetadataBlocksPointers(byte[] bytes) {
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        decoder.readInteger(1); // file type  TODO: maybe assert that it is correct?
        this.metadataBlocks = decoder.readList(MAX_COUNT_DATA_BLOCKS, METADATA_BLOCK_POINTER_SIZE_BYTES);
        this.maxDepth = decoder.readInteger(1);
    }

    public MetadataBlocksPointers(int maxDepth) {
        this.metadataBlocks = new ArrayList<>();
        this.maxDepth = maxDepth;
    }

    @Override
    public byte[] toBytes() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(FILE_TYPE.id, 1);
        encoder.writeList(metadataBlocks, MAX_COUNT_DATA_BLOCKS, METADATA_BLOCK_POINTER_SIZE_BYTES);
        encoder.writeInt(maxDepth, 1);
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
        return metadataBlocks.size() < MAX_COUNT_DATA_BLOCKS;
    }

    public List<Integer> getMetadataBlocks() {
        return metadataBlocks;
    }

    public void addDataBlock(int blockId) {
        this.metadataBlocks.add(blockId);
    }

    public void deleteLastDataBlock() {
        this.metadataBlocks.remove(this.metadataBlocks.size()-1);
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
