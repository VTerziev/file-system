package jb.filesystem.files;

import jb.filesystem.files.accessors.DirectoryAccessorI;
import jb.filesystem.files.accessors.FileAccessorI;
import jb.filesystem.blocks.blockmanager.MetadataBlocksManager;
import jb.filesystem.blocks.metadata.FileType;
import jb.filesystem.blocks.metadata.MetadataBlock;

public class FileFactory {
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
