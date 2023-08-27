package jb.filesystem.init;

import jb.filesystem.blockmanager.DataBlocksManager;
import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.blockmanager.StoredDataBlocksManager;
import jb.filesystem.blockmanager.StoredMetadataManager;
import jb.filesystem.storage.*;

public class DataBlockManagersProvider {
    private final MetadataBlocksManager metadataBlockManager;
    private final DataBlocksManager dataBlockManager;

    public DataBlockManagersProvider(ByteStorage storage) {
        StorageSegmentor segmentor = new StorageSegmentor(storage);
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
