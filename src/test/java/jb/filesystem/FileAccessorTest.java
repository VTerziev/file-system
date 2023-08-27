package jb.filesystem;

import jb.filesystem.blockmanager.DataBlocksManager;
import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.blockmanager.StoredDataBlocksManager;
import jb.filesystem.blockmanager.StoredMetadataManager;
import jb.filesystem.accessors.FileAccessor;
import jb.filesystem.storage.BitStorage;
import jb.filesystem.traversing.TraversablesFactory;
import jb.filesystem.traversing.Traversor;
import jb.filesystem.utils.PersistentBitmask;
import jb.filesystem.metadata.FileMetadata;
import jb.filesystem.data.DataBlock;
import jb.filesystem.storage.DataBlocksStorage;
import jb.filesystem.storage.InMemoryStorage;
import jb.filesystem.storage.MetadataStorage;
import org.junit.Test;

import java.util.Arrays;

public class FileAccessorTest {

    @Test
    public void test() {
        BitStorage bitStorage = new BitStorage(new InMemoryStorage(30));
        PersistentBitmask metadataBitmask = new PersistentBitmask(30, bitStorage);
        MetadataStorage metadataStorage = new MetadataStorage(new InMemoryStorage(300));
        MetadataBlocksManager metadataManager = new StoredMetadataManager(metadataBitmask, metadataStorage);

        BitStorage bitStorage2 = new BitStorage(new InMemoryStorage(30));
        PersistentBitmask dataBitmask = new PersistentBitmask(30, bitStorage2);
        DataBlocksStorage dataStorage = new DataBlocksStorage(new InMemoryStorage(300));
        DataBlocksManager dataBlocksManager = new StoredDataBlocksManager(dataBitmask, dataStorage);

        TraversablesFactory factory = new TraversablesFactory(metadataManager);
        Traversor traversor = new Traversor();
        FileAccessor fileAccessor = new FileAccessor(metadataManager, dataBlocksManager, factory, traversor);

        int fileId = metadataManager.allocateBlock();
        FileMetadata metadata = new FileMetadata("file1");
        metadataManager.saveBlock(fileId, metadata);

        fileAccessor.writeToAFile(fileId, 0, new byte[]{'z', 'z', 'z'}, 3);
        metadata = (FileMetadata) metadataManager.getBlock(fileId);

        System.out.println(metadata.getName());
        System.out.println(metadata.getFileSize());
        System.out.println(metadata.getDataBlocks());

        int blockId = metadata.getDataBlocks().get(0);
        DataBlock d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));
        System.out.println("File size:" + metadata.getFileSize());


        fileAccessor.writeToAFile(fileId, 1, new byte[]{'x', 'x', 'x'}, 3);

        metadata = (FileMetadata) metadataManager.getBlock(fileId);
        System.out.println(metadata.getName());
        System.out.println(metadata.getFileSize());
        System.out.println(metadata.getDataBlocks());

        blockId = metadata.getDataBlocks().get(0);
        d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));
        System.out.println("File size:" + metadata.getFileSize());


        fileAccessor.writeToAFile(fileId, 4, new byte[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'z'}, 14);

        metadata = (FileMetadata) metadataManager.getBlock(fileId);
        System.out.println(metadata.getName());
        System.out.println(metadata.getFileSize());
        System.out.println(metadata.getDataBlocks());

        blockId = metadata.getDataBlocks().get(0);
        d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));

        blockId = metadata.getDataBlocks().get(1);
        d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));
        System.out.println("File size:" + metadata.getFileSize());
    }

    @Test
    public void test2() {
        BitStorage bitStorage = new BitStorage(new InMemoryStorage(30));
        PersistentBitmask metadataBitmask = new PersistentBitmask(30, bitStorage);
        MetadataStorage metadataStorage = new MetadataStorage(new InMemoryStorage(300));
        MetadataBlocksManager metadataManager = new StoredMetadataManager(metadataBitmask, metadataStorage);

        BitStorage bitStorage2 = new BitStorage(new InMemoryStorage(30));
        PersistentBitmask dataBitmask = new PersistentBitmask(30, bitStorage2);
        DataBlocksStorage dataStorage = new DataBlocksStorage(new InMemoryStorage(300));
        DataBlocksManager dataBlocksManager = new StoredDataBlocksManager(dataBitmask, dataStorage);

        TraversablesFactory factory = new TraversablesFactory(metadataManager);
        Traversor traversor = new Traversor();
        FileAccessor fileAccessor = new FileAccessor(metadataManager, dataBlocksManager, factory, traversor);

        int fileId = metadataManager.allocateBlock();
        FileMetadata metadata = new FileMetadata("file1");
        metadataManager.saveBlock(fileId, metadata);

        fileAccessor.writeToAFile(fileId, 0, new byte[]{'z', 'z', 'z'}, 3);
        metadata = (FileMetadata) metadataManager.getBlock(fileId);

        System.out.println(metadata.getName());
        System.out.println(metadata.getFileSize());
        System.out.println(metadata.getDataBlocks());

        int blockId = metadata.getDataBlocks().get(0);
        DataBlock d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));
        System.out.println("File size:" + metadata.getFileSize());

        fileAccessor.writeToAFile(fileId, 1, new byte[]{'x', 'x', 'x'}, 3);

        metadata = (FileMetadata) metadataManager.getBlock(fileId);
        System.out.println(metadata.getName());
        System.out.println(metadata.getFileSize());
        System.out.println(metadata.getDataBlocks());

        blockId = metadata.getDataBlocks().get(0);
        d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));
        System.out.println("File size:" + metadata.getFileSize());


        fileAccessor.writeToAFile(fileId, 4, new byte[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'z'}, 14);

        metadata = (FileMetadata) metadataManager.getBlock(fileId);
        System.out.println(metadata.getName());
        System.out.println(metadata.getFileSize());
        System.out.println(metadata.getDataBlocks());

        blockId = metadata.getDataBlocks().get(0);
        d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));

        blockId = metadata.getDataBlocks().get(1);
        d = dataBlocksManager.getBlock(blockId);
        System.out.println(Arrays.toString(d.getBytes()));
        System.out.println("File size:" + metadata.getFileSize());

        byte[] buffer = new byte[10];
        fileAccessor.readFromFile(fileId, 0, 5, buffer);
        System.out.println(Arrays.toString(buffer));
    }

    @Test
    public void testtest() {
        BitStorage bitStorage = new BitStorage(new InMemoryStorage(30));
        PersistentBitmask metadataBitmask = new PersistentBitmask(30, bitStorage);
        MetadataStorage metadataStorage = new MetadataStorage(new InMemoryStorage(300));
        MetadataBlocksManager metadataManager = new StoredMetadataManager(metadataBitmask, metadataStorage);

        BitStorage bitStorage2 = new BitStorage(new InMemoryStorage(30));
        PersistentBitmask dataBitmask = new PersistentBitmask(30, bitStorage2);
        DataBlocksStorage dataStorage = new DataBlocksStorage(new InMemoryStorage(300));
        DataBlocksManager dataBlocksManager = new StoredDataBlocksManager(dataBitmask, dataStorage);

        TraversablesFactory factory = new TraversablesFactory(metadataManager);
        Traversor traversor = new Traversor();
        FileAccessor fileAccessor = new FileAccessor(metadataManager, dataBlocksManager, factory, traversor);

        int fileId = metadataManager.allocateBlock();
        FileMetadata metadata = new FileMetadata("file1");
        metadataManager.saveBlock(fileId, metadata);

        fileAccessor.writeToAFile(fileId, 0, new byte[]{'z', 'z', 'z'}, 3);
        System.out.println(getAllFileContent(fileId, fileAccessor, metadataManager));
        fileAccessor.writeToAFile(fileId, 1, new byte[]{'z', 'z', 'z'}, 3);
        System.out.println(getAllFileContent(fileId, fileAccessor, metadataManager));

        fileAccessor.writeToAFile(fileId, 0, new byte[]{'x', 'x', 'x'}, 3);
        System.out.println(getAllFileContent(fileId, fileAccessor, metadataManager));

        fileAccessor.writeToAFile(fileId, 4, new byte[]{'y', 'y', 'y'}, 2);
        System.out.println(getAllFileContent(fileId, fileAccessor, metadataManager));

        fileAccessor.writeToAFile(fileId, 1, new byte[]{'z', 'z', 'z'}, 3);
        System.out.println(getAllFileContent(fileId, fileAccessor, metadataManager));

    }

    private String getAllFileContent(int fileId, FileAccessor dataManager, MetadataBlocksManager metadataManager) {
        FileMetadata block = (FileMetadata) metadataManager.getBlock(fileId);
        byte[] b = new byte[block.getFileSize()];
        dataManager.readFromFile(fileId, 0, block.getFileSize(), b);

        StringBuilder ret = new StringBuilder();
        for (byte value : b) {
            ret.append((char) value);
        }
        return ret.toString();
    }

}