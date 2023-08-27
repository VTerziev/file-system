package jb.filesystem.storage;

import jb.filesystem.data.DataBlock;

public class DataBlocksStorage extends TypedStorage<DataBlock> {

    public DataBlocksStorage (ByteStorage storage) {
        super(storage);
    }

    @Override
    int getByteSizeOfT() {
        return DataBlock.BLOCK_SIZE_BYTES;
    }

    @Override
    DataBlock decodeT(byte[] bytes) {
        return new DataBlock(bytes);
    }

    @Override
    byte[] encodeT(DataBlock dataBlock) {
        return dataBlock.getBytes();
    }
}
