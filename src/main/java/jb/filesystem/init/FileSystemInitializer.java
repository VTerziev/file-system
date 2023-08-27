package jb.filesystem.init;

import jb.filesystem.files.FileFactory;
import jb.filesystem.FileSystemI;
import jb.filesystem.FileSystemImp;
import jb.filesystem.blockmanager.DataBlocksManager;
import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.accessors.DirectoryAccessor;
import jb.filesystem.accessors.FileAccessor;
import jb.filesystem.storage.ByteStorage;
import jb.filesystem.traversing.TraversablesFactory;
import jb.filesystem.traversing.Traversor;

public class FileSystemInitializer {
    private final FileAccessor fileAccessor;
    private final DirectoryAccessor directoryAccessor;
    private final FileFactory fileFactory;

    public FileSystemInitializer(ByteStorage storage) {
        DataBlockManagersProvider provider = new DataBlockManagersProvider(storage);
        DataBlocksManager dataBlockManager = provider.getDataBlockManager();
        MetadataBlocksManager metadataBlockManager = provider.getMetadataBlockManager();
        TraversablesFactory factory = new TraversablesFactory(metadataBlockManager);
        Traversor traversor = new Traversor();
        this.fileAccessor = new FileAccessor(metadataBlockManager, dataBlockManager, factory, traversor);
        this.directoryAccessor = new DirectoryAccessor(metadataBlockManager, fileAccessor);
        this.fileFactory = new FileFactory(metadataBlockManager, directoryAccessor, fileAccessor);
    }

    public FileSystemI init() {
        String rootDirName = "root";
        int rootDir = directoryAccessor.createDirectory(rootDirName);
        rootDir = directoryAccessor.createDirectory(rootDirName); // TODO: fix duplication

        return new FileSystemImp(fileAccessor, directoryAccessor, fileFactory, rootDir);
    }
}
