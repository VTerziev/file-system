package jb.filesystem.blocks.metadata;

import jb.filesystem.init.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetadataBlocksPointersTest {
    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void toAndFromBytes() {
        MetadataBlocksPointers metadata = new MetadataBlocksPointers(2);
        metadata.addDataBlock(42);
        metadata.addDataBlock(43);

        MetadataBlocksPointers convertedMetadata = new MetadataBlocksPointers(metadata.toBytes());

        Assert.assertEquals(metadata.getName(), convertedMetadata.getName());
        Assert.assertEquals(metadata.getMaxDepth(), convertedMetadata.getMaxDepth());
        Assert.assertEquals(metadata.getMetadataBlocks(), convertedMetadata.getMetadataBlocks());
    }
}
