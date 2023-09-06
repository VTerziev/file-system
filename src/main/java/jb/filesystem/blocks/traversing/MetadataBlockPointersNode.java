package jb.filesystem.blocks.traversing;

import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.metadata.DataBlocksPointers;
import jb.filesystem.blocks.metadata.MetadataBlock;
import jb.filesystem.blocks.metadata.MetadataBlocksPointers;

import java.util.List;
import java.util.stream.Collectors;

import static jb.filesystem.config.FileSystemConfig.CONFIG;

public class MetadataBlockPointersNode implements TreeNode {
    private final MetadataBlocksPointers pointers;
    private final int id;
    private final MetadataBlocksManager metadataManager;

    public MetadataBlockPointersNode(MetadataBlocksPointers pointers, int id,
                                     MetadataBlocksManager metadataManager) {
        this.pointers = pointers;
        this.id = id;
        this.metadataManager = metadataManager;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean hasDirectLeafSlotsAvailable() {
        return false;
    }

    @Override
    public void appendDirectLeaf(int leafId) {
        throw new IllegalStateException("No slots available");
    }

    @Override
    public int getDirectLeaf(int leafOffset) {
        throw new IllegalArgumentException("Leaf offset out of bounds");
    }

    @Override
    public void deleteLastDirectLeaf() {
        throw new IllegalStateException("No leaves to delete");
    }

    @Override
    public void deleteLastNonLeaf() {
        metadataManager.deallocateBlock(pointers.getMetadataBlocks().get(pointers.getMetadataBlocks().size()-1));
        pointers.deleteLastDataBlock();
        metadataManager.saveBlock(id, pointers);
    }

    @Override
    public int getDirectLeavesCount() {
        return 0;
    }

    @Override
    public List<TreeNode> getNonLeaves() {
        return pointers.getMetadataBlocks().stream()
                .map(this::getNonLeaf)
                .collect(Collectors.toList());
    }

    private boolean shouldUseDataBlockPointers() {
        return pointers.getMaxDepth() == 1;
    }

    private TreeNode getNonLeaf(int blockId) {
        if (shouldUseDataBlockPointers()) {
            DataBlocksPointers block = metadataManager.getDataBlocksPointersMetadata(blockId);
            return new DataBlockPointersNode(block, blockId, metadataManager);
        } else {
            MetadataBlocksPointers block = metadataManager.getMetadataBlocksPointersMetadata(blockId);
            return new MetadataBlockPointersNode(block, blockId, metadataManager);
        }
    }

    @Override
    public void createNewNonLeaf() {
        int blockId = metadataManager.allocateBlock();
        int maxDepth = pointers.getMaxDepth();
        MetadataBlock dataBlocksPointers = shouldUseDataBlockPointers()
                ? new DataBlocksPointers()
                : new MetadataBlocksPointers(maxDepth-1);
        metadataManager.saveBlock(blockId, dataBlocksPointers);
        pointers.addDataBlock(blockId);
        metadataManager.saveBlock(id, pointers);
    }

    @Override
    public boolean nonLeafSlotsFull() {
        return !pointers.hasDataBlocksSlotsAvailable();
    }

    @Override
    public int getMaxLeafCapacity() {
        return (int) (Math.pow(CONFIG.METADATA_POINTERS_MAX_COUNT_CHILDREN, pointers.getMaxDepth())
                        * CONFIG.DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN);
    }
}
