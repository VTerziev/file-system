package jb.filesystem.blocks.metadata;

import jb.filesystem.blocks.metadata.DirectoryMetadata;
import jb.filesystem.blocks.metadata.FileType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DirectoryMetadataTest {

    // TODO: more tests

    @Test
    public void directoryTest() {
        DirectoryMetadata metadata = new DirectoryMetadata(new byte[]{
                FileType.DIRECTORY.id,
                'd', 'i', 'r', 0, 0,
                0, 1, 0, 2, 0, 3, 0, 4, 0, 5});

        Assert.assertEquals("dir", metadata.getName());
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5), metadata.getChildren());
    }
}
