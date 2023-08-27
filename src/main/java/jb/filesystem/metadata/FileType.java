package jb.filesystem.metadata;

public enum FileType {
    REGULAR((byte)1),
    DIRECTORY((byte)2),
    DATA_BLOCKS_POINTERS((byte)3),
    METADATA_BLOCKS_POINTERS((byte)4),
    ;

    public final byte id;
    FileType(byte id) {
        this.id = id;
    }
}
