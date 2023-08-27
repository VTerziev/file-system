package jb.filesystem.storage;

import jb.filesystem.metadata.MetadataBlock;
import jb.filesystem.metadata.MetadataBlocksFactory;

public class MetadataStorage extends TypedStorage<MetadataBlock> {
    private final MetadataBlocksFactory factory;

    public MetadataStorage (ByteStorage storage) {
        super(storage);
        this.factory = new MetadataBlocksFactory(); // TODO: give as parameter
    }

    @Override
    int getByteSizeOfT() {
        return MetadataBlock.BLOCK_SIZE_BYTES;
    }

    @Override
    MetadataBlock decodeT(byte[] bytes) {
        return factory.buildBlock(bytes);
    }

    @Override
    byte[] encodeT(MetadataBlock block) {
        return block.toBytes();
    }
}
