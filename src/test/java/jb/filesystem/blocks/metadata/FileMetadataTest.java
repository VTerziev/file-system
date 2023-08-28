package jb.filesystem.blocks.metadata;

import jb.filesystem.blocks.metadata.FileMetadata;
import jb.filesystem.init.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class FileMetadataTest {
    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void toAndFromBytes() {
        FileMetadata metadata = new FileMetadata("file");
        metadata.getDataBlocks().add(14);
        metadata.getDataBlocks().add(13);
        metadata.maybeIncreaseFileSize(72);
        metadata.appendIndirectBlock(42);

        FileMetadata convertedMetadata = new FileMetadata(metadata.toBytes());

        Assert.assertEquals(metadata.getName(), convertedMetadata.getName());
        Assert.assertEquals(metadata.getDataBlocks(), convertedMetadata.getDataBlocks());
        Assert.assertEquals(metadata.getFileSize(), convertedMetadata.getFileSize());
        Assert.assertEquals(metadata.getIndirectDataBlocks(), convertedMetadata.getIndirectDataBlocks());
    }
}