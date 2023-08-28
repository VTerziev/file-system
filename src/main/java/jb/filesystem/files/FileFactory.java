package jb.filesystem.files;

import jb.filesystem.accessors.DirectoryAccessorI;
import jb.filesystem.accessors.FileAccessorI;
import jb.filesystem.blockmanager.MetadataBlocksManager;
import jb.filesystem.metadata.FileType;
import jb.filesystem.metadata.MetadataBlock;

public class FileFactory { // TODO: rename
    private final MetadataBlocksManager metadataManager;
    private final DirectoryAccessorI directoryAccessor;
    private final FileAccessorI fileAccessor;

    public FileFactory(MetadataBlocksManager metadataManager,
                       DirectoryAccessorI directoryAccessor,
                       FileAccessorI fileAccessor) {
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
