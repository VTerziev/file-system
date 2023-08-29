package jb.filesystem.init;

import jb.filesystem.storage.*;
import jb.filesystem.utils.PersistentBitmask;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

/**
 * Splits a given ByteStorage into different segments, to be used by a FileSystem.
 */
public class StorageSegmentor {
    private final PersistentBitmask dataBitmask;
    private final PersistentBitmask metadataBitmask;
    private final DataBlocksStorage dataBlocksStorage;
    private final MetadataStorage metadataStorage;

    public StorageSegmentor(ByteStorage storage) {
        ByteStorage dataBitmaskStorage = new StorageView(storage, getDataBitmaskSegmentStart(), CONFIG.DATA_BITMASK_SIZE);
        BitStorage dataBitmaskBitStorage = new BitStorage(dataBitmaskStorage);
        this.dataBitmask = new PersistentBitmask(CONFIG.DATA_BITMASK_SIZE*CONFIG.BITS_IN_BYTE, dataBitmaskBitStorage);

        ByteStorage metadataBitmaskStorage = new StorageView(storage, getMetadataBitmaskSegmentStart(), CONFIG.METADATA_BITMASK_SIZE);
        BitStorage metadataBitmaskBitStorage = new BitStorage(metadataBitmaskStorage);
        this.metadataBitmask = new PersistentBitmask(CONFIG.METADATA_BITMASK_SIZE*CONFIG.BITS_IN_BYTE, metadataBitmaskBitStorage);

        ByteStorage dataSegmentView = new StorageView(storage, getDataSegmentStart(), CONFIG.DATA_SEGMENT_SIZE);
        this.dataBlocksStorage = new DataBlocksStorage(dataSegmentView);

        ByteStorage metadataSegmentView = new StorageView(storage, getMetadataSegmentStart(), CONFIG.METADATA_SEGMENT_SIZE);
        this.metadataStorage = new MetadataStorage(metadataSegmentView);
    }

    public PersistentBitmask getDataBlocksBitmask() {
        return dataBitmask;
    }

    public PersistentBitmask getMetadataBitmask() {
        return metadataBitmask;
    }

    public DataBlocksStorage getDataBlocksStorage() {
        return dataBlocksStorage;
    }

    public MetadataStorage getMetadataStorage() {
        return metadataStorage;
    }

    private int getMetadataBitmaskSegmentStart() {
        return CONFIG.INITIAL_OFFSET;
    }

    private int getDataBitmaskSegmentStart() {
        return getMetadataBitmaskSegmentStart()+CONFIG.METADATA_BITMASK_SIZE;
    }

    private int getMetadataSegmentStart() {
        return getDataBitmaskSegmentStart() + CONFIG.DATA_BITMASK_SIZE;
    }

    private int getDataSegmentStart() {
        return getMetadataSegmentStart() + CONFIG.METADATA_SEGMENT_SIZE;
    }
}
