package jb.filesystem.traversing;

import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.metadata.DataBlocksPointers;
import jb.filesystem.metadata.MetadataBlock;
import jb.filesystem.metadata.MetadataBlocksPointers;

import java.util.List;
import java.util.stream.Collectors;

public class MetadataBlockPointersTraversable implements Traversable {
    private final MetadataBlocksPointers pointers;
    private final int id;
    private final MetadataBlocksManager metadataManager;

    public MetadataBlockPointersTraversable(MetadataBlocksPointers pointers, int id,
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
    public List<Traversable> getNonLeaves() { // TODO: don't create new factories here; same for different traversable impl
        return pointers.getMetadataBlocks().stream()
                .map(blockId -> {
                    MetadataBlock block = metadataManager.getBlock(blockId);
                    return new TraversablesFactory(metadataManager).buildTraversable(block, blockId);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void createNewNonLeaf() {
        int blockId = metadataManager.allocateBlock();
        int maxDepth = pointers.getMaxDepth();
        MetadataBlock dataBlocksPointers = maxDepth == 1 ?
                new DataBlocksPointers() : new MetadataBlocksPointers(maxDepth-1);
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
        return (int) (Math.pow(MetadataBlocksPointers.MAX_COUNT_DATA_BLOCKS, pointers.getMaxDepth())
                        * DataBlocksPointers.MAX_COUNT_DATA_BLOCKS);
    }
}
