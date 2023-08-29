package jb.filesystem.blocks.traversing;

import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.metadata.DataBlocksPointers;

import java.util.Collections;
import java.util.List;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

public class DataBlockPointersNode implements TreeNode {

    private final DataBlocksPointers pointers;
    private final int id;
    private final MetadataBlocksManager metadataManager;

    public DataBlockPointersNode(DataBlocksPointers pointers, int id, MetadataBlocksManager metadataManager) {
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
        return pointers.hasDataBlocksSlotsAvailable();
    }

    @Override
    public void appendDirectLeaf(int leafId) {
        if (hasDirectLeafSlotsAvailable()) {
            pointers.addDataBlock(leafId);
            metadataManager.saveBlock(id, pointers);
            return;
        }
        throw new IllegalStateException("No slots available");
    }

    @Override
    public int getDirectLeaf(int leafOffset) {
        if (pointers.getDataBlocks().size() > leafOffset) {
            return pointers.getDataBlocks().get(leafOffset);
        }
        throw new IllegalArgumentException("Leaf offset out of bounds");
    }

    @Override
    public void deleteLastDirectLeaf() {
        pointers.removeLastDataBlock();
        metadataManager.saveBlock(id, pointers);
    }

    @Override
    public void deleteLastNonLeaf() {
        throw new IllegalStateException("Doesn't have any non leaves");
    }

    @Override
    public int getDirectLeavesCount() {
        return pointers.getDataBlocks().size();
    }

    @Override
    public List<TreeNode> getNonLeaves() {
        return Collections.emptyList();
    }

    @Override
    public void createNewNonLeaf() {
        throw new IllegalStateException("Can't create a non-leaf");
    }

    @Override
    public boolean nonLeafSlotsFull() {
        return true;
    }

    @Override
    public int getMaxLeafCapacity() {
        return CONFIG.DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN;
    }
}
