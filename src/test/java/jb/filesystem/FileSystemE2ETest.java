package jb.filesystem;

import jb.filesystem.files.FileI;
import jb.filesystem.init.FileSystemInitializer;
import jb.filesystem.storage.ByteStorage;
import jb.filesystem.storage.InMemoryStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemE2ETest {
    private FileSystemInitializer initializer;

    @Before
    public void setup() {
        ByteStorage storage = new InMemoryStorage(3000);
        initializer = new FileSystemInitializer(storage);
    }

    @Test
    public void testCreatingFilesAndFolders() {
        FileSystemI fs = initializer.init();

        Assert.assertEquals(Collections.emptyList(), fs.listFiles("/"));

        // Create one file
        fs.createFile("/", "file1");
        Assert.assertEquals(List.of("file1"), listFileNamesIn(fs, "/"));

        // Create one directory
        fs.createDirectory("/", "dir1");
        Assert.assertEquals(List.of("dir1", "file1"), listFileNamesIn(fs, "/"));
        Assert.assertEquals(List.of(), listFileNamesIn(fs, "/dir1"));

        // Create one file inside the directory
        fs.createFile("/dir1", "file2");
        Assert.assertEquals(List.of("dir1", "file1"), listFileNamesIn(fs, "/"));
        Assert.assertEquals(List.of("file2"), listFileNamesIn(fs, "/dir1"));
    }

    @Test
    public void testDeletingFilesAndFolders() {
        FileSystemI fs = initializer.init();

        // Creating the following structure:
        // -> /
        // ->   file1
        // ->   dir1/
        // ->       file2
        // ->       file3
        // ->       dir2/
        // ->           file4
        // ->           file5
        fs.createFile("/", "file1");
        fs.createDirectory("/", "dir1");
        fs.createFile("/dir1", "file2");
        fs.createFile("/dir1", "file3");
        fs.createDirectory("/dir1", "dir2");
        fs.createFile("/dir1/dir2", "file4");
        fs.createFile("/dir1/dir2", "file5");

        // Deleting file1
        Assert.assertEquals(List.of("dir1", "file1"), listFileNamesIn(fs, "/"));
        fs.delete("/", "file1");
        Assert.assertEquals(List.of("dir1"), listFileNamesIn(fs, "/"));

        // Deleting file4
        Assert.assertEquals(List.of("file4", "file5"), listFileNamesIn(fs, "/dir1/dir2"));
        fs.delete("/dir1/dir2", "file4");
        Assert.assertEquals(List.of("file5"), listFileNamesIn(fs, "/dir1/dir2"));

        // Deleting the whole folder dir1 and its contents
        fs.delete("/", "dir1");
        Assert.assertEquals(List.of(), listFileNamesIn(fs, "/"));
    }


    @Test
    public void testPersistenceToStorage() {
        // Create a few files through one FileSystem instance
        {
            FileSystemI fs = initializer.init();
            // Create a few files
            fs.createFile("/", "file1");
            fs.createDirectory("/", "dir1");
            fs.createFile("/dir1", "file2");
        }

        // Assert the files are there when you create another instance
        {
            FileSystemI fs2 = initializer.init();
            Assert.assertEquals(List.of("dir1", "file1"), listFileNamesIn(fs2, "/"));
            Assert.assertEquals(List.of("file2"), listFileNamesIn(fs2, "/dir1"));
        }
    }

    @Test
    public void writeAndReadFromFile() {
        FileSystemI fs = initializer.init();
        fs.createFile("/", "file1");
        FileI file = fs.get("/", "file1").get();

        byte[] buffer = new byte[20];
        Assert.assertTrue(file.isRegularFile());
        Assert.assertEquals("file1", file.getName());
        Assert.assertEquals(10, file.write(0, 10, "asdfasdfasdf".getBytes()));
        Assert.assertEquals(10, file.read(0, 10, buffer));
        Assert.assertEquals(10, file.getSize());
        Assert.assertEquals("asdfasdfas", toString(buffer, 10));

        Assert.assertEquals(10, file.write(5, 10, "zzzzzzzzzz".getBytes()));
        Assert.assertEquals(15, file.read(0, 15, buffer));
        Assert.assertEquals(15, file.getSize());
        Assert.assertEquals("asdfazzzzzzzzzz", toString(buffer, 15));
    }

    private List<String> listFileNamesIn(FileSystemI fs, String dir) {
        return fs.listFiles(dir).stream().map(FileI::getName).sorted().collect(Collectors.toList());
    }

    private String toString(byte[] bytes, int len) {
        return new String(bytes).substring(0, len);
    }
}