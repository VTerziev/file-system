package jb.filesystem.storage;

import jb.filesystem.blocks.data.DataBlock;

import static jb.filesystem.config.FileSystemConfig.CONFIG;

public class DataBlocksStorage extends TypedStorage<DataBlock> {

    public DataBlocksStorage (ByteStorage storage) {
        super(storage);
    }

    @Override
    protected int getByteSizeOfT() {
        return CONFIG.DATA_BLOCK_SIZE_BYTES;
    }

    @Override
    protected DataBlock decodeT(byte[] bytes) {
        return new DataBlock(bytes);
    }

    @Override
    protected byte[] encodeT(DataBlock dataBlock) {
        return dataBlock.getBytes();
    }
}
