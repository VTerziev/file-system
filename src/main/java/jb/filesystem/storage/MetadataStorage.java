package jb.filesystem.storage;

import jb.filesystem.blocks.metadata.MetadataBlock;
import jb.filesystem.blocks.metadata.MetadataBlocksFactory;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

public class MetadataStorage extends TypedStorage<MetadataBlock> {
    private final MetadataBlocksFactory factory;

    public MetadataStorage (ByteStorage storage) {
        super(storage);
        this.factory = new MetadataBlocksFactory(); // TODO: inject, instead of constructing here
    }

    @Override
    protected int getByteSizeOfT() {
        return CONFIG.METADATA_BLOCK_SIZE_BYTES;
    }

    @Override
    protected MetadataBlock decodeT(byte[] bytes) {
        return factory.buildBlock(bytes);
    }

    @Override
    protected byte[] encodeT(MetadataBlock block) {
        return block.toBytes();
    }
}
