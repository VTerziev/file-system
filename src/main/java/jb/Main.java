package jb;

import jb.filesystem.files.FileI;
import jb.filesystem.FileSystemI;
import jb.filesystem.init.FileSystemInitializer;
import jb.filesystem.storage.FileStorage;
import jb.filesystem.storage.ByteStorage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        File fileHandle = new File("./src/main/resources/mediumFS");

        try (RandomAccessFile fileForStorage = new RandomAccessFile(fileHandle, "rw")) {
            ByteStorage storage = new FileStorage(fileForStorage);
            FileSystemInitializer initializer = new FileSystemInitializer(storage);
            FileSystemI fs = initializer.init();

//            System.out.println(fs.get("/dir1"));
//            System.out.println(fs.get("/file2"));
//            System.out.println(fs.get("/dir1/file3"));

            Optional<FileI> file = fs.get("/dir1/file3");
            byte[] buffer = new byte[12];
            file.get().read(0, 12, buffer);

            System.out.println(new String(buffer));
        }
    }
}
