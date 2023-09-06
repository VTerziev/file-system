package jb.filesystem.blocks.metadata;

import jb.filesystem.config.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class MetadataBlocksDecoderTest {
    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void readIntegers() {
        byte[] initial = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(initial);
        // Read single bytes
        Assert.assertEquals(0, decoder.readInteger(1));
        Assert.assertEquals(1, decoder.readInteger(1));
        Assert.assertEquals(2, decoder.readInteger(1));

        // Read multiple bytes
        Assert.assertEquals(3*256 + 4, decoder.readInteger(2));
        Assert.assertEquals(5*256*256 + 6*256 + 7, decoder.readInteger(3));
    }

    @Test
    public void readString() {
        byte[] initial = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 0,0,0,0,0,0,0,0,0,0};
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(initial);

        // Read single letters
        Assert.assertEquals("a", decoder.readString(1));
        Assert.assertEquals("b", decoder.readString(1));

        // Read multiple letters
        Assert.assertEquals("cdef", decoder.readString(4));
    }


    @Test
    public void readList() {
        byte[] initial = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(initial);

        Assert.assertEquals(List.of(1, 2*256+3, 4*256+5), decoder.readList(3, 2));
    }
}