package jb.filesystem.blocks.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.config.FileSystemConfig.CONFIG;

/**
 * A metadata block, which contains only a list of pointers to other metadata blocks
 */
public class MetadataBlocksPointers implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.METADATA_BLOCKS_POINTERS;
    private final List<Integer> metadataBlocks;
    private final int maxDepth;

    public MetadataBlocksPointers(byte[] bytes) {
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        int fileType = decoder.readInteger(1);
        if (fileType != FILE_TYPE.id) {
            throw new IllegalArgumentException("The file type does not match");
        }
        this.metadataBlocks = decoder.readList(CONFIG.METADATA_POINTERS_MAX_COUNT_CHILDREN,
                CONFIG.METADATA_BLOCK_POINTER_SIZE_BYTES);
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
        encoder.writeList(metadataBlocks, CONFIG.METADATA_POINTERS_MAX_COUNT_CHILDREN,
                CONFIG.METADATA_BLOCK_POINTER_SIZE_BYTES);
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
        return metadataBlocks.size() < CONFIG.METADATA_POINTERS_MAX_COUNT_CHILDREN;
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
