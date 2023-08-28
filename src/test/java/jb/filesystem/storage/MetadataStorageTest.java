package jb.filesystem.storage;

import jb.filesystem.blocks.metadata.DirectoryMetadata;
import jb.filesystem.blocks.metadata.FileMetadata;
import jb.filesystem.blocks.metadata.FileType;
import jb.filesystem.blocks.metadata.MetadataBlock;
import jb.filesystem.init.FileSystemConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetadataStorageTest {

    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void writeAndRead() {
        ByteStorage inner = new InMemoryStorage(50);
        MetadataStorage storage = new MetadataStorage(inner);

        Assert.assertEquals(2, storage.write(0, 2,
                new MetadataBlock[]{new FileMetadata("file"), new DirectoryMetadata("dir")}));

        MetadataBlock[] buffer = new MetadataBlock[3];
        Assert.assertEquals(2, storage.read(0, 2, buffer));
        Assert.assertEquals(FileType.REGULAR, buffer[0].getType());
        Assert.assertEquals("file", buffer[0].getName());

        Assert.assertEquals(FileType.DIRECTORY, buffer[1].getType());
        Assert.assertEquals("dir", buffer[1].getName());
    }

}