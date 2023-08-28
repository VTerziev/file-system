package jb.filesystem.blocks.metadata;

public interface MetadataBlock {

    byte[] toBytes();

    FileType getType();

    String getName(); // TODO: remove this
}