package jb.filesystem.blocks.metadata;

import jb.filesystem.config.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class MetadataBlocksEncoderTest {

    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void writeOneByteInteger() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(1, 16);
        byte[] result = encoder.getResult();
        // All are 0 except the last one
        for (int i = 0 ; i < 15 ; i ++ ) {
            Assert.assertEquals(0, result[i]);
        }
        Assert.assertEquals(1, result[15]);
    }

    @Test
    public void writeMultipleBytesInteger() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(256*256 + 2*256 + 3, 16); // 256*256 + 2*256 + 3 should be written 1,2,3 as bytes
        byte[] result = encoder.getResult();
        // All are 0 except the last 3
        for (int i = 0 ; i < 13 ; i ++ ) {
            Assert.assertEquals(0, result[i]);
        }
        Assert.assertEquals(1, result[13]);
        Assert.assertEquals(2, result[14]);
        Assert.assertEquals(3, result[15]);
    }

    @Test
    public void writeString() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeString("abc", 5);
        byte[] result = encoder.getResult();
        for (int i = 3 ; i < 16 ; i ++ ) {
            Assert.assertEquals(0, result[i]);
        }
        Assert.assertEquals('a', result[0]);
        Assert.assertEquals('b', result[1]);
        Assert.assertEquals('c', result[2]);
    }

    @Test
    public void writeMultipleValues() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(42, 1);
        encoder.writeString("abc", 4);
        encoder.writeList(List.of(1,2,3,4), 5, 1);

        byte[] result = encoder.getResult();
        // The number
        Assert.assertEquals(42, result[0]);

        // The string
        Assert.assertEquals('a', result[1]);
        Assert.assertEquals('b', result[2]);
        Assert.assertEquals('c', result[3]);
        Assert.assertEquals(0, result[4]);

        // The list
        Assert.assertEquals(1, result[5]);
        Assert.assertEquals(2, result[6]);
        Assert.assertEquals(3, result[7]);
        Assert.assertEquals(4, result[8]);
    }
}
