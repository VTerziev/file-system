package jb.filesystem.init;

import jb.filesystem.blocks.blockmanager.DataBlocksManager;
import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.blockmanager.StoredDataBlocksManager;
import jb.filesystem.blocks.blockmanager.StoredMetadataManager;
import jb.filesystem.storage.*;

public class DataBlockManagersProvider {
    private final MetadataBlocksManager metadataBlockManager;
    private final DataBlocksManager dataBlockManager;

    public DataBlockManagersProvider(ByteStorage storage, StorageSegmentor segmentor) {
        this.dataBlockManager =
                new StoredDataBlocksManager(segmentor.getDataBlocksBitmask(), segmentor.getDataBlocksStorage());
        this.metadataBlockManager =
                new StoredMetadataManager(segmentor.getMetadataBitmask(), segmentor.getMetadataStorage());
    }

    public MetadataBlocksManager getMetadataBlockManager() {
        return this.metadataBlockManager;
    }

    public DataBlocksManager getDataBlockManager() {
        return this.dataBlockManager;
    }

}
