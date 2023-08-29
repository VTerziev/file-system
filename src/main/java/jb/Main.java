package jb;

import jb.filesystem.files.FileI;
import jb.filesystem.FileSystemI;
import jb.filesystem.init.FileSystemInitializer;
import jb.filesystem.storage.FileStorage;
import jb.filesystem.storage.ByteStorage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String[] args) throws IOException {

        File fileHandle = new File("./src/main/resources/fileAsStorage");

        try (RandomAccessFile fileForStorage = new RandomAccessFile(fileHandle, "rw")) {
            ByteStorage storage = new FileStorage(fileForStorage);
            FileSystemInitializer initializer = new FileSystemInitializer(storage);
            FileSystemI fs = initializer.init();

//            fs.createDirectory("/", "dir1");
            System.out.println(fs.get("/dir1"));

            System.out.println(fs.get("/file1"));
            FileI file = fs.createFile("/", "file2");
            System.out.println(fs.get("/file2"));

//            FileI file2 = fs.createFile("/","file2");
//            System.out.println(fs.get("/","file2"));
//
//            int cc = 0;
//            for (int i = 0 ; i < 10 ; i ++ ) {
//                file2.write(cc, 20, new byte[]{(byte)i, (byte) (i/256), 'c', 'd', 'e',
//                        'a', 'b', 'c', 'd', 'e', 'a', 'b', 'c', 'd', 'e',
//                        'a', 'b', 'c', 'd', 'e' });
//                byte[] buffer = new byte[20];
//                file2.read(cc, 20, buffer);
//                cc += 20;
//
//                System.out.println("Next 20 bytes are: " + Arrays.toString(buffer));
//            }
//            fs.delete("/","file2");
//            fs.delete("/","file1");
//
//            fs.createDirectory("/", "dir1");
//            fs.createFile("/dir1", "ff");
//
//            System.out.println(fs.get("/", "ff"));
//            System.out.println(fs.get("/dir1", "ff"));
//
//            fs.rename("/dir1", "ff", "dd");
//            System.out.println(fs.get("/dir1", "ff"));
//            System.out.println(fs.get("/dir1", "dd"));
//
//            fs.createFile("/dir1", "ff");
//            System.out.println(fs.listFiles("/"));
//            System.out.println(fs.listFiles("/dir1"));
//
//
//            fs.rename("/", "dir1", "dir2");
//            System.out.println(fs.listFiles("/"));
//            System.out.println(fs.get("/", "dir1"));
//            System.out.println(fs.get("/", "dir2"));
        } finally {

        }
    }
}
