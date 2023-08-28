package jb.filesystem.metadata;

import jb.filesystem.blocks.metadata.FileMetadata;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class FileMetadataTest {

    // TODO: add more tests
    @Test
    public void createFromByteArray() {
        byte[] b = new byte[]{'a', 's', 'd', 'f', 0, 0,
                0, 1,
                1, 1,
                0, 0,
                0, 0,
                0, 0};
        FileMetadata m = new FileMetadata(b);

        Assert.assertEquals("asdf", m.getName());
        Assert.assertEquals(Arrays.asList(1, 257), m.getDataBlocks());
    }

    @Test
    public void toAndFromBytes() {
        FileMetadata metadata = new FileMetadata("file");
        metadata.getDataBlocks().add(14);
        metadata.getDataBlocks().add(13);
        metadata.maybeIncreaseFileSize(72);
        FileMetadata metadata2 = new FileMetadata(metadata.toBytes());

        Assert.assertEquals(metadata.getName(), metadata2.getName());
        System.out.println(metadata2.getDataBlocks());
        Assert.assertEquals(metadata.getDataBlocks(), metadata2.getDataBlocks());
        System.out.println(metadata.getFileSize());
        Assert.assertEquals(metadata.getFileSize(), metadata2.getFileSize());
    }
}