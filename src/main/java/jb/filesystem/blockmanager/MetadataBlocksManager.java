package jb.filesystem.blockmanager;

import jb.filesystem.metadata.*;

public interface MetadataBlocksManager {
    MetadataBlock getBlock(int blockId);
    DirectoryMetadata getDirectoryMetadata(int blockId);
    FileMetadata getFileMetadata(int blockId);
    DataBlocksPointers getDataBlocksPointersMetadata(int blockId);
    MetadataBlocksPointers getMetadataBlocksPointersMetadata(int blockId);

    void saveBlock(int blockId, MetadataBlock block);

    int allocateBlock();

    void deallocateBlock(int blockId);

}
