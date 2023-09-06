package jb.filesystem.blocks.metadata;

/**
 * Represents a block, which contains the metadata of some file. Could be used for storing the name of the file, the
 * location of its data, access rights, etc.
 */
public interface MetadataBlock {

    byte[] toBytes();

    FileType getType();

    String getName(); // TODO: remove this
}