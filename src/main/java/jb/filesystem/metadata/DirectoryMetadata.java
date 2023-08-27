package jb.filesystem.metadata;

import java.util.ArrayList;
import java.util.List;

public class DirectoryMetadata implements MetadataBlock {
    private static final FileType FILE_TYPE = FileType.DIRECTORY;
    private final int NAME_SIZE = 5;
    private final int MAX_COUNT_CHILDREN = 5;
    private String name;
    private final List<Integer> childrenMetadataBlocks;

    public DirectoryMetadata(byte[] bytes) {
        // TODO: assert the byte size? use preconditions?
        if (bytes.length != MetadataBlock.BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException("Incorrect number of bytes");
        }

        // TODO: refactor this somehow
        if (MetadataBlock.BLOCK_SIZE_BYTES != 1+ NAME_SIZE + METADATA_BLOCK_POINTER_SIZE_BYTES *MAX_COUNT_CHILDREN) {
            throw new IllegalArgumentException("Incorrect number of bytes");
        }

        MetadataBlocksDecoder decoder = new MetadataBlocksDecoder(bytes);
        decoder.readInteger(1); // file type  TODO: maybe assert that it is correct?
        this.name = decoder.readString(NAME_SIZE);
        this.childrenMetadataBlocks = decoder.readList(MAX_COUNT_CHILDREN, METADATA_BLOCK_POINTER_SIZE_BYTES);
    }

    public DirectoryMetadata(String name) {
        this.name = name;
        this.childrenMetadataBlocks = new ArrayList<>();
    }

    @Override
    public byte[] toBytes() {
        MetadataBlocksEncoder encoder = new MetadataBlocksEncoder();
        encoder.writeInt(FILE_TYPE.id, 1);
        encoder.writeString(name, NAME_SIZE);
        encoder.writeList(childrenMetadataBlocks, MAX_COUNT_CHILDREN, METADATA_BLOCK_POINTER_SIZE_BYTES);
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
        name = newName; // TODO: test this
    }

    public List<Integer> getChildren() {
        return childrenMetadataBlocks;
    }

    public void addChild(int fileId) {
        // TODO: some safety checks
        childrenMetadataBlocks.add(fileId);
    }

    public void deleteChild(int fileId) {
        childrenMetadataBlocks.remove((Integer) fileId); // TODO: test this
    }
}
