package jb.filesystem.traversing;

import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.metadata.FileMetadata;
import jb.filesystem.metadata.MetadataBlock;
import jb.filesystem.metadata.MetadataBlocksPointers;

import java.util.ArrayList;
import java.util.List;

public class RegularFileTraversable implements Traversable {
    private final FileMetadata fileMetadata;
    private final int id;
    private final MetadataBlocksManager metadataManager;

    public RegularFileTraversable(FileMetadata fileMetadata, int id,
                                  MetadataBlocksManager metadataManager) {
        this.fileMetadata = fileMetadata;
        this.id = id;
        this.metadataManager = metadataManager;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean hasDirectLeafSlotsAvailable() {
        return fileMetadata.hasDataBlocksSlotsAvailable();
    }

    @Override
    public void appendDirectLeaf(int leafId) {
        if (hasDirectLeafSlotsAvailable()) {
            fileMetadata.addDataBlock(leafId);
            metadataManager.saveBlock(id, fileMetadata);
            return;
        }
        throw new IllegalStateException("No slots available");
    }

    @Override
    public int getDirectLeaf(int leafOffset) {
        if (fileMetadata.getDataBlocks().size() > leafOffset) {
            return fileMetadata.getDataBlocks().get(leafOffset);
        }
        throw new IllegalArgumentException("Leaf offset out of bounds");
    }

    @Override
    public void deleteLastDirectLeaf() {
        fileMetadata.removeLastDataBlock();
        metadataManager.saveBlock(id, fileMetadata);
    }

    @Override
    public void deleteLastNonLeaf() {
        int idToDelete = fileMetadata.getIndirectDataBlocks().get(fileMetadata.getIndirectDataBlocks().size()-1);
        metadataManager.deallocateBlock(idToDelete);
        fileMetadata.removeLastIndirectDataBlock();
        metadataManager.saveBlock(id, fileMetadata);
    }

    @Override
    public int getDirectLeavesCount() {
        return fileMetadata.getDataBlocks().size();
    }

    @Override
    public List<Traversable> getNonLeaves() {
        List<Traversable> result = new ArrayList<>();

        for (int blockId : fileMetadata.getIndirectDataBlocks()) {
            MetadataBlocksPointers block = metadataManager.getMetadataBlocksPointersMetadata(blockId);
            Traversable traversable = new MetadataBlockPointersTraversable(block, blockId, metadataManager);
            result.add(traversable);
        }
        return result;
    }

    @Override
    public void createNewNonLeaf() {
        int blockId = metadataManager.allocateBlock();
        MetadataBlocksPointers dataBlocksPointers = new MetadataBlocksPointers(2);
        metadataManager.saveBlock(blockId, dataBlocksPointers);

        if (fileMetadata.areIndirectSlotsFull()) {
            throw new IllegalStateException("Can't add more traversables");
        }
        fileMetadata.appendIndirectBlock(blockId);
        metadataManager.saveBlock(id, fileMetadata);
    }

    @Override
    public boolean nonLeafSlotsFull() {
        return fileMetadata.areIndirectSlotsFull();
    }

    @Override
    public int getMaxLeafCapacity() {
        throw new UnsupportedOperationException();
    }
}
