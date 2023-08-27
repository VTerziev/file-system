package jb.filesystem.traversing;

import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.metadata.DataBlocksPointers;

import java.util.Collections;
import java.util.List;

public class DataBlockPointersTraversable implements Traversable {

    private final DataBlocksPointers pointers;
    private final int id;
    private final MetadataBlocksManager metadataManager;

    public DataBlockPointersTraversable(DataBlocksPointers pointers, int id, MetadataBlocksManager metadataManager) {
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
    public List<Traversable> getNonLeaves() {
        return Collections.emptyList();
    }

    @Override
    public void createNewNonLeaf() {
        throw new IllegalStateException("Can't create a traversable");
    }

    @Override
    public boolean nonLeafSlotsFull() {
        return true;
    }

    @Override
    public int getMaxLeafCapacity() {
        return DataBlocksPointers.MAX_COUNT_DATA_BLOCKS;
    }
}
