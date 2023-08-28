package jb.filesystem.blocks.metadata;

public class MetadataBlocksFactory {

    public MetadataBlock buildBlock(byte[] bytes) {
        if (bytes[0] == FileType.REGULAR.id) {
            return new FileMetadata(bytes);
        } else if (bytes[0] == FileType.DIRECTORY.id) {
            return new DirectoryMetadata(bytes);
        } else if (bytes[0] == FileType.DATA_BLOCKS_POINTERS.id) {
            return new DataBlocksPointers(bytes);
        } else if (bytes[0] == FileType.METADATA_BLOCKS_POINTERS.id) {
            return new MetadataBlocksPointers(bytes);
        }
        throw new IllegalArgumentException("No matching id");
    }
}
