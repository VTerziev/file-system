package jb.filesystem.blocks.traversing;

import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.blockmanager.StoredMetadataManager;
import jb.filesystem.blocks.metadata.FileMetadata;
import jb.filesystem.init.FileSystemConfig;
import jb.filesystem.storage.BitStorage;
import jb.filesystem.storage.InMemoryStorage;
import jb.filesystem.storage.MetadataStorage;
import jb.filesystem.utils.PersistentBitmask;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TraversorTest {

    private MetadataBlocksManager manager;

    @BeforeClass
    public static void setup() {
        FileSystemConfig.setDefaultConfig();
    }

    @Test
    public void addLeavesAndReadThem() {
        manager = buildMetadataManager();
        FileMetadata metadata = new FileMetadata("file");
        int blockId = manager.allocateBlock();
        manager.saveBlock(blockId, metadata);
        // TODO: use mocks
        TreeNode node = new RegularFileNode(metadata, blockId, manager);
        Traversor traversor = new Traversor();

        Assert.assertEquals(0, traversor.getTotalLeavesCount(node));
        traversor.appendLeaf(node, 42);
        Assert.assertEquals(List.of(42), readAllLeaves(traversor, node));

        traversor.appendLeaf(node, 43);
        Assert.assertEquals(List.of(42, 43), readAllLeaves(traversor, node));

        traversor.appendLeaf(node, 44);
        traversor.appendLeaf(node, 45);
        Assert.assertEquals(List.of(42, 43, 44, 45), readAllLeaves(traversor, node));
    }

    @Test
    public void readWriteAndDeleteLeaves() {
        manager = buildMetadataManager();
        FileMetadata metadata = new FileMetadata("file");
        int blockId = manager.allocateBlock();
        manager.saveBlock(blockId, metadata);
        // TODO: use mocks
        TreeNode node = new RegularFileNode(metadata, blockId, manager);
        Traversor traversor = new Traversor();

        Assert.assertEquals(0, traversor.getTotalLeavesCount(node));
        traversor.appendLeaf(node, 42);
        Assert.assertEquals(List.of(42), readAllLeaves(traversor, node));

        traversor.appendLeaf(node, 43);
        Assert.assertEquals(List.of(42, 43), readAllLeaves(traversor, node));

        traversor.deleteLastLeaf(node);
        Assert.assertEquals(List.of(42), readAllLeaves(traversor, node));

        traversor.appendLeaf(node, 44);
        Assert.assertEquals(List.of(42, 44), readAllLeaves(traversor, node));

        traversor.deleteLastLeaf(node);
        traversor.deleteLastLeaf(node);
        Assert.assertEquals(List.of(), readAllLeaves(traversor, node));
    }

    private List<Integer> readAllLeaves(Traversor traversor, TreeNode node) {
        List<Integer> result = new LinkedList<>();
        for (int i = 0 ; i < traversor.getTotalLeavesCount(node) ; i ++ ) {
            result.add(traversor.getLeaf(node, i));
        }
        return result;
    }

    private MetadataBlocksManager buildMetadataManager() {
        PersistentBitmask bitmask = new PersistentBitmask(30, new BitStorage(new InMemoryStorage(30)));
        MetadataStorage metadataStorage = new MetadataStorage(new InMemoryStorage(1000));
        return new StoredMetadataManager(bitmask, metadataStorage);
    }
}