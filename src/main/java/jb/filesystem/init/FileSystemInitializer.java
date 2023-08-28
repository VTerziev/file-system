package jb.filesystem.init;

import jb.filesystem.accessors.*;
import jb.filesystem.files.FileFactory;
import jb.filesystem.FileSystemI;
import jb.filesystem.FileSystemImp;
import jb.filesystem.blockmanager.DataBlocksManager;
import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.storage.ByteStorage;
import jb.filesystem.traversing.Traversor;

public class FileSystemInitializer { // TODO: maybe refactor the constructor
    private final FileAccessorI fileAccessor;
    private final DirectoryAccessorI directoryAccessor;
    private final FileFactory fileFactory;

    public FileSystemInitializer(ByteStorage storage) {
        DataBlockManagersProvider provider = new DataBlockManagersProvider(storage);
        DataBlocksManager dataBlockManager = provider.getDataBlockManager();
        MetadataBlocksManager metadataBlockManager = provider.getMetadataBlockManager();
        Traversor traversor = new Traversor();
        FileLocksProvider locksProvider = new SimpleLocksProvider(StorageSegmentor.COUNT_METADATA_BLOCKS);

        this.fileAccessor = buildFileAccessor(metadataBlockManager, dataBlockManager, traversor, locksProvider);
        this.directoryAccessor = buildDirectoryAccessor(metadataBlockManager, fileAccessor, locksProvider);
        this.fileFactory = new FileFactory(metadataBlockManager, directoryAccessor, fileAccessor);
    }

    public FileSystemI init() {
        String rootDirName = "root";
        int rootDir = directoryAccessor.createDirectory(rootDirName);
        rootDir = directoryAccessor.createDirectory(rootDirName); // TODO: fix duplication

        return new FileSystemImp(fileAccessor, directoryAccessor, fileFactory, rootDir);
    }

    private FileAccessorI buildFileAccessor(MetadataBlocksManager metadataBlockManager,
                                            DataBlocksManager dataBlocksManager,
                                            Traversor traversor, FileLocksProvider locksProvider) {
        FileAccessorI inner = new FileAccessor(metadataBlockManager, dataBlocksManager, traversor);
        return new SynchronizedFileAccessor(inner, locksProvider);
    }

    private DirectoryAccessorI buildDirectoryAccessor(MetadataBlocksManager metadataBlocksManager,
                                                     FileAccessorI fileAccessor, FileLocksProvider locksProvider) {
        DirectoryAccessorI inner = new DirectoryAccessor(metadataBlocksManager, fileAccessor);
        return new SynchronizedDirectoryAccessor(inner, locksProvider);
    }
}
