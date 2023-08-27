package jb.filesystem.traversing;

import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.metadata.*;

// TODO: is it a factory?
public class TraversablesFactory {

    private final MetadataBlocksManager metadataManager;

    public TraversablesFactory(MetadataBlocksManager metadataManager) {
        this.metadataManager = metadataManager;
    }

    public Traversable buildTraversable(MetadataBlock block, int id) {
        if (block.getType() == FileType.REGULAR) {
            return new RegularFileTraversable((FileMetadata) block, id, metadataManager);
        } else if (block.getType() == FileType.DIRECTORY) { // TODO
            throw new IllegalArgumentException("Directory is not traversable");
        } else if (block.getType() == FileType.DATA_BLOCKS_POINTERS) {
            return new DataBlockPointersTraversable((DataBlocksPointers) block, id, metadataManager);
        } else if (block.getType() == FileType.METADATA_BLOCKS_POINTERS) {
            return new MetadataBlockPointersTraversable((MetadataBlocksPointers) block, id, metadataManager);
        }

        throw new IllegalArgumentException("No matching id");
    }
}
