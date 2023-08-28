package jb.filesystem.blocks.metadata;

import jb.filesystem.init.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataBlocksPointersTest {
    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void toAndFromBytes() {
        DataBlocksPointers metadata = new DataBlocksPointers();
        metadata.addDataBlock(42);
        metadata.addDataBlock(43);

        DataBlocksPointers convertedMetadata = new DataBlocksPointers(metadata.toBytes());

        Assert.assertEquals(metadata.getName(), convertedMetadata.getName());
        Assert.assertEquals(metadata.getDataBlocks(), convertedMetadata.getDataBlocks());
    }
}