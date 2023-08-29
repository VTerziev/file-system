package jb.filesystem.blocks.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.config.FileSystemConfig.CONFIG;

/**
 * A metadata block, containing all the information about a given directory. Contains a directory name and a list of
 * files (metadata block ids), which are contained inside.
 */
public class DirectoryMetadata implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.DIRECTORY;
    private String name;
    private final List<Integer> childrenMetadataBlocks;

    public DirectoryMetadata(byte[] bytes) {
        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        int fileType = decoder.readInteger(1);
        if (fileType != FILE_TYPE.id) {
            throw new IllegalArgumentException("The file type does not match");
        }
        this.name = decoder.readString(CONFIG.DIRECTORY_METADATA_NAME_SIZE);
        this.childrenMetadataBlocks = decoder.readList(CONFIG.DIRECTORY_METADATA_MAX_COUNT_CHILDREN, CONFIG.METADATA_BLOCK_POINTER_SIZE_BYTES);
    }

    public DirectoryMetadata(String name) {
        this.name = name;
        this.childrenMetadataBlocks = new ArrayList<>();
    }

    @Override
    public byte[] toBytes() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(FILE_TYPE.id, 1);
        encoder.writeString(name, CONFIG.DIRECTORY_METADATA_NAME_SIZE);
        encoder.writeList(childrenMetadataBlocks, CONFIG.DIRECTORY_METADATA_MAX_COUNT_CHILDREN, CONFIG.METADATA_BLOCK_POINTER_SIZE_BYTES);
        return encoder.getResult();
    }

    @Override
    public FileType getType() {
        return FileType.DIRECTORY;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        if (newName.length() > CONFIG.DIRECTORY_METADATA_NAME_SIZE) {
            throw new IllegalArgumentException("Name too long");
        }
        name = newName;
    }

    public List<Integer> getChildren() {
        return childrenMetadataBlocks;
    }

    public void addChild(int fileId) {
        if (childrenMetadataBlocks.size() >= CONFIG.DIRECTORY_METADATA_MAX_COUNT_CHILDREN) {
            throw new IllegalStateException("Too many children");
        }
        childrenMetadataBlocks.add(fileId);
    }

    public void deleteChild(int fileId) {
        childrenMetadataBlocks.remove((Integer) fileId);
    }
}
