package jb.filesystem.config;

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

    public final long INITIAL_OFFSET;
    public final long COUNT_METADATA_BLOCKS;
    public final long COUNT_DATA_BLOCKS;
    public final int DATA_BITMASK_SIZE;
    public final int METADATA_BITMASK_SIZE;
    public final long METADATA_SEGMENT_SIZE;
    public final long DATA_SEGMENT_SIZE;

    public static FileSystemConfig CONFIG;

    public FileSystemConfig(long storageSize) {
        BITS_IN_BYTE = 8;

        // Data blocks constants
        DATA_BLOCK_SIZE_BYTES = chooseValueI(storageSize, 16, 32, 1024);
        DATA_BLOCK_POINTER_SIZE_BYTES = chooseValueI(storageSize, 2, 2, 4);
        ;  // TODO: maybe compute log from the total bytes

        // Metadata blocks constants
        METADATA_BLOCK_SIZE_BYTES = chooseValueI(storageSize, 16, 32, 512);
        METADATA_BLOCK_POINTER_SIZE_BYTES = chooseValueI(storageSize, 2, 2, 4);
        ; // TODO: maybe compute log from the total bytes

        // FileMetadata constants
        FILE_METADATA_NAME_SIZE = chooseValueI(storageSize, 5, 10, 25);
        FILE_METADATA_FILE_SIZE_BYTES = chooseValueI(storageSize, 2, 4, 4);
        ;
        FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT = 2;
        FILE_METADATA_MAX_COUNT_DATA_BLOCKS = chooseValueI(storageSize, 2, 5, 100);

        //DirectoryMetadata constants
        DIRECTORY_METADATA_NAME_SIZE = chooseValueI(storageSize, 5, 15, 25);
        DIRECTORY_METADATA_MAX_COUNT_CHILDREN =
                (METADATA_BLOCK_SIZE_BYTES - DIRECTORY_METADATA_NAME_SIZE) / METADATA_BLOCK_POINTER_SIZE_BYTES;

        //MetadataBlocksPointers constants
        METADATA_POINTERS_MAX_COUNT_CHILDREN = (METADATA_BLOCK_SIZE_BYTES - 1) / METADATA_BLOCK_POINTER_SIZE_BYTES;

        //DataBlocksPointers constants
        DATA_BLOCK_POINTERS_MAX_COUNT_CHILDREN = (METADATA_BLOCK_SIZE_BYTES - 1) / DATA_BLOCK_POINTER_SIZE_BYTES;
        ;

        INITIAL_OFFSET = 10;
        COUNT_METADATA_BLOCKS = chooseValue(storageSize, 30, 60, 1000000);
        COUNT_DATA_BLOCKS = chooseValue(storageSize, 100, 125, 1000000);
        DATA_BITMASK_SIZE = (int) Math.ceil(((double) COUNT_DATA_BLOCKS) / BITS_IN_BYTE);
        METADATA_BITMASK_SIZE = (int) Math.ceil(((double) COUNT_METADATA_BLOCKS) / BITS_IN_BYTE);

        METADATA_SEGMENT_SIZE = COUNT_METADATA_BLOCKS * METADATA_BLOCK_SIZE_BYTES;
        DATA_SEGMENT_SIZE = COUNT_DATA_BLOCKS * DATA_BLOCK_SIZE_BYTES;

        if (METADATA_SEGMENT_SIZE + DATA_SEGMENT_SIZE + METADATA_BITMASK_SIZE + DATA_BITMASK_SIZE > storageSize) {
            System.out.println(METADATA_SEGMENT_SIZE + " " + DATA_SEGMENT_SIZE + " " + METADATA_BITMASK_SIZE + " " + DATA_BITMASK_SIZE);
            throw new IllegalStateException("Segments are too big");
        }

        // Assert the metadata blocks size add up correctly
        // TODO: refactor this somehow
        if (METADATA_BLOCK_SIZE_BYTES < 1 + FILE_METADATA_NAME_SIZE +
                DATA_BLOCK_POINTER_SIZE_BYTES * FILE_METADATA_MAX_COUNT_DATA_BLOCKS +
                FILE_METADATA_FILE_SIZE_BYTES +
                FILE_METADATA_MAX_INDIRECT_DATA_BLOCKS_COUNT * METADATA_BLOCK_POINTER_SIZE_BYTES
        ) {
            throw new IllegalStateException("Incorrect number of bytes");
        }

        // TODO: refactor this somehow
        if (METADATA_BLOCK_SIZE_BYTES < 1 + DIRECTORY_METADATA_NAME_SIZE +
                METADATA_BLOCK_POINTER_SIZE_BYTES * DIRECTORY_METADATA_MAX_COUNT_CHILDREN) {
            throw new IllegalStateException("Incorrect number of bytes");
        }
    }

    // TODO: use DI instead of this
    public static void setStaticConfig(FileSystemConfig config) {
        CONFIG = config;
    }


    public static void setDefaultConfig() {
        CONFIG = new FileSystemConfig(3000); // TODO: is this the best option
    }

    // TODO: make it more customizable
    private static long chooseValue(long storageSize, long smallFSVal, long mediumFSVal, long largeFSVal) {
        if (storageSize < 3000) {
            throw new IllegalArgumentException("Storage size too small");
        } else if (storageSize < 10000000) {
            return smallFSVal;
        } else if (storageSize < 5000000000L) {
            return mediumFSVal;
        } else {
            return largeFSVal;
        }
    }

    private static int chooseValueI(long storageSize, long smallFSVal, long mediumFSVal, long largeFSVal) {
        return (int) chooseValue(storageSize, smallFSVal, mediumFSVal, largeFSVal);
    }
}