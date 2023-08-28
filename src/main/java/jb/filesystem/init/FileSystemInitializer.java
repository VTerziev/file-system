package jb.filesystem.init;

import jb.filesystem.files.FileFactory;
import jb.filesystem.FileSystemI;
import jb.filesystem.FileSystemImp;
import jb.filesystem.blocks.blockmanager.DataBlocksManager;
import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.files.accessors.DirectoryAccessor;
import jb.filesystem.files.accessors.DirectoryAccessorI;
import jb.filesystem.files.accessors.FileAccessor;
import jb.filesystem.files.accessors.FileAccessorI;
import jb.filesystem.storage.ByteStorage;
import jb.filesystem.files.synchronization.FileLocksProvider;
import jb.filesystem.files.synchronization.SimpleLocksProvider;
import jb.filesystem.files.synchronization.SynchronizedDirectoryAccessor;
import jb.filesystem.files.synchronization.SynchronizedFileAccessor;
import jb.filesystem.blocks.traversing.Traversor;
import jb.filesystem.utils.PathUtils;

import static jb.filesystem.init.FileSystemConfig.CONFIG;

public class FileSystemInitializer {
    private final FileAccessorI fileAccessor;
    private final DirectoryAccessorI directoryAccessor;
    private final FileFactory fileFactory;
    private final PathUtils pathUtils;

    public FileSystemInitializer(ByteStorage storage) { // TODO: maybe refactor the constructor
        FileSystemConfig config = new FileSystemConfig(storage.getSize());
        FileSystemConfig.setStaticConfig(config);

        StorageSegmentor segmentor = new StorageSegmentor(storage);
        DataBlockManagersProvider provider = new DataBlockManagersProvider(storage, segmentor);
        DataBlocksManager dataBlockManager = provider.getDataBlockManager();
        MetadataBlocksManager metadataBlockManager = provider.getMetadataBlockManager();
        Traversor traversor = new Traversor();
        FileLocksProvider locksProvider = new SimpleLocksProvider(CONFIG.COUNT_METADATA_BLOCKS);

        this.fileAccessor = buildFileAccessor(metadataBlockManager, dataBlockManager, traversor, locksProvider);
        this.directoryAccessor = buildDirectoryAccessor(metadataBlockManager, fileAccessor, locksProvider);
        this.fileFactory = new FileFactory(metadataBlockManager, directoryAccessor, fileAccessor);
        this.pathUtils = new PathUtils();
    }

    public FileSystemI init() {
        String rootDirName = "root";
        int rootDir = directoryAccessor.createDirectory(rootDirName);
        return new FileSystemImp(fileAccessor, directoryAccessor, fileFactory, rootDir, pathUtils);
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
