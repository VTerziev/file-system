package jb.filesystem.init;

public class FileSystemConfig {
    public final int BITS_IN_BYTE;

    // Data blocks constants
    public final int DATA_BLOCK_SIZE_BYTES;
    public final int DATA_BLOCK_POINTER_SIZE_BYTES;

    // Metadata blocks constants
    public final int METADATA_BLOCK_SIZE_BYTES;
    public final int METADATA_BLOCK_POINTER_SIZE_BYTES;

    // FileMetadata constants
    public final int FILE_METADATA_NAME_SIZE;
    public final int FILE_METADATA_MAX_COUNT_DATA_BLOCKS;
    public final int FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT;
    public final int FILE_METADATA_FILE_SIZE_BYTES;

    //DirectoryMetadata constants
    public final int DIRECTORY_METADATA_NAME_SIZE;
    public final int DIRECTORY_METADATA_MAX_COUNT_CHILDREN;

    //MetadataBlocksPointers constants
    public final int METADATA_POINTERS_MAX_COUNT_CHILDREN;

    //DataBlocksPointers constants
    public final int DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN;

    public final int INITIAL_OFFSET;
    public final int COUNT_METADATA_BLOCKS;
    public final int COUNT_DATA_BLOCKS;
    public final int DATA_BITMASK_SIZE;
    public final int METADATA_BITMASK_SIZE;
    public final int METADATA_SEGMENT_SIZE;
    public final int DATA_SEGMENT_SIZE;

    public static FileSystemConfig CONFIG;

    public FileSystemConfig(int storageSize) {
        BITS_IN_BYTE = 8;

        // Data blocks constants
        DATA_BLOCK_SIZE_BYTES = 16;
        DATA_BLOCK_POINTER_SIZE_BYTES = 2;  // TODO: maybe compute log from the total bytes


        // Metadata blocks constants
        METADATA_BLOCK_SIZE_BYTES = 16;
        METADATA_BLOCK_POINTER_SIZE_BYTES = 2; // TODO: maybe compute log from the total bytes


        // FileMetadata constants
        FILE_METADATA_NAME_SIZE = 5;
        FILE_METADATA_MAX_COUNT_DATA_BLOCKS = 2;
        FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT = 2;
        FILE_METADATA_FILE_SIZE_BYTES = 2;

        //DirectoryMetadata constants
        DIRECTORY_METADATA_NAME_SIZE = 5;
        DIRECTORY_METADATA_MAX_COUNT_CHILDREN = 5;

        //MetadataBlocksPointers constants
        METADATA_POINTERS_MAX_COUNT_CHILDREN = 7;

        //DataBlocksPointers constants
        DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN = 7;

        INITIAL_OFFSET = 10;
        COUNT_METADATA_BLOCKS = 30;
        COUNT_DATA_BLOCKS = 150;
        DATA_BITMASK_SIZE = (int) Math.ceil(((double) COUNT_DATA_BLOCKS)/BITS_IN_BYTE);
        METADATA_BITMASK_SIZE = (int) Math.ceil(((double) COUNT_METADATA_BLOCKS)/BITS_IN_BYTE);

        METADATA_SEGMENT_SIZE = COUNT_METADATA_BLOCKS*METADATA_BLOCK_SIZE_BYTES;
        DATA_SEGMENT_SIZE = COUNT_DATA_BLOCKS*DATA_BLOCK_SIZE_BYTES;

        // Assert the metadata blocks size add up correctly
        // TODO: refactor this somehow
        if (METADATA_BLOCK_SIZE_BYTES != 1 + FILE_METADATA_NAME_SIZE +
                DATA_BLOCK_POINTER_SIZE_BYTES*FILE_METADATA_MAX_COUNT_DATA_BLOCKS +
                FILE_METADATA_FILE_SIZE_BYTES +
                FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT*METADATA_BLOCK_POINTER_SIZE_BYTES
        ) {
            throw new IllegalArgumentException("Incorrect number of bytes");
        }

        // TODO: refactor this somehow
        if (METADATA_BLOCK_SIZE_BYTES != 1 + DIRECTORY_METADATA_NAME_SIZE +
                METADATA_BLOCK_POINTER_SIZE_BYTES*DIRECTORY_METADATA_MAX_COUNT_CHILDREN) {
            throw new IllegalArgumentException("Incorrect number of bytes");
        }
    }

    // TODO: use DI instead of this
    public static void setStaticConfig(FileSystemConfig config) {
        CONFIG = config;
    }


    public static void setDefaultConfig() {
        CONFIG = new FileSystemConfig(100); // TODO: is this the best option
    }
}
