package jb.filesystem.files;

import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.accessors.DirectoryAccessor;
import jb.filesystem.accessors.FileAccessor;
import jb.filesystem.metadata.FileType;
import jb.filesystem.metadata.MetadataBlock;

public class FileFactory { // TODO: rename
    private final MetadataBlocksManager metadataManager;
    private final DirectoryAccessor directoryAccessor;
    private final FileAccessor fileAccessor;

    public FileFactory(MetadataBlocksManager metadataManager,
                       DirectoryAccessor directoryAccessor,
                       FileAccessor fileAccessor) {
        this.metadataManager = metadataManager;
        this.directoryAccessor = directoryAccessor;
        this.fileAccessor = fileAccessor;
    }

    public FileI wrapFile(int fileId) {
        MetadataBlock block = metadataManager.getBlock(fileId);
        if (block.getType() == FileType.DIRECTORY) {
            return new DirectoryFile(fileId, directoryAccessor);
        } else if (block.getType() == FileType.REGULAR){
            return new RegularFile(fileId, fileAccessor);
        }
        throw new IllegalArgumentException("File type not found");
    }
}
