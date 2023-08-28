package jb.filesystem.init;

import jb.filesystem.data.DataBlock;
import jb.filesystem.metadata.MetadataBlock;
import jb.filesystem.storage.*;
import jb.filesystem.utils.PersistentBitmask;

public class StorageSegmentor {
    private static final int INITIAL_OFFSET = 10;
    private static final int BITS_IN_BYTE = 8;
    public static final int COUNT_METADATA_BLOCKS = 30; // TODO: move to a config file
    private static final int COUNT_DATA_BLOCKS = 150;
    private static final int DATA_BITMASK_SIZE = (int) Math.ceil(((double) COUNT_DATA_BLOCKS)/BITS_IN_BYTE);
    private static final int METADATA_BITMASK_SIZE = (int) Math.ceil(((double) COUNT_METADATA_BLOCKS)/BITS_IN_BYTE);
    private static final int METADATA_SEGMENT_SIZE = COUNT_METADATA_BLOCKS* MetadataBlock.BLOCK_SIZE_BYTES;
    private static final int DATA_SEGMENT_SIZE = COUNT_DATA_BLOCKS*DataBlock.BLOCK_SIZE_BYTES;

    private final PersistentBitmask dataBitmask;
    private final PersistentBitmask metadataBitmask;
    private final DataBlocksStorage dataBlocksStorage;
    private final MetadataStorage metadataStorage;

    public StorageSegmentor(ByteStorage storage) {
        ByteStorage dataBitmaskStorage = new StorageView(storage, getDataBitmaskSegmentStart(), DATA_BITMASK_SIZE);
        BitStorage dataBitmaskBitStorage = new BitStorage(dataBitmaskStorage);
        this.dataBitmask = new PersistentBitmask(DATA_BITMASK_SIZE*BITS_IN_BYTE, dataBitmaskBitStorage);

        ByteStorage metadataBitmaskStorage = new StorageView(storage, getMetadataBitmaskSegmentStart(), METADATA_BITMASK_SIZE);
        BitStorage metadataBitmaskBitStorage = new BitStorage(metadataBitmaskStorage);
        this.metadataBitmask = new PersistentBitmask(METADATA_BITMASK_SIZE*BITS_IN_BYTE, metadataBitmaskBitStorage);

        ByteStorage dataSegmentView = new StorageView(storage, getDataSegmentStart(), DATA_SEGMENT_SIZE);
        this.dataBlocksStorage = new DataBlocksStorage(dataSegmentView);

        ByteStorage metadataSegmentView = new StorageView(storage, getMetadataSegmentStart(), METADATA_SEGMENT_SIZE);
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
        return INITIAL_OFFSET;
    }

    private int getDataBitmaskSegmentStart() {
        return getMetadataBitmaskSegmentStart()+METADATA_BITMASK_SIZE;
    }

    private int getMetadataSegmentStart() {
        return getDataBitmaskSegmentStart() + DATA_BITMASK_SIZE;
    }

    private int getDataSegmentStart() {
        return getMetadataSegmentStart() + METADATA_SEGMENT_SIZE;
    }

}
