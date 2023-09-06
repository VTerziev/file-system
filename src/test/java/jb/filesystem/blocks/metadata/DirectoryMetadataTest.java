package jb.filesystem.blocks.metadata;

import jb.filesystem.config.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectoryMetadataTest {
    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void toAndFromBytes() {
        DirectoryMetadata metadata = new DirectoryMetadata("dir");
        metadata.addChild(42);
        metadata.addChild(43);

        DirectoryMetadata convertedMetadata = new DirectoryMetadata(metadata.toBytes());

        Assert.assertEquals(metadata.getName(), convertedMetadata.getName());
        Assert.assertEquals(metadata.getChildren(), convertedMetadata.getChildren());
    }
}
