package jb.filesystem.metadata;

public interface MetadataBlock {
    int BLOCK_SIZE_BYTES = 16;// TODO: maybe move to a config class
    int METADATA_BLOCK_POINTER_SIZE_BYTES = 2; // TODO: maybe compute log from the total bytes

    byte[] toBytes();

    FileType getType();

    String getName(); // TODO: remove this
}