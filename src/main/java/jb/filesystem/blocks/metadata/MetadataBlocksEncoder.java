package jb.filesystem.blocks.metadata;

import java.util.List;

import static jb.filesystem.config.FileSystemConfig.CONFIG;

/**
 * A helper class, which supports encoding a metadata block into a byte array.
 */
public class MetadataBlocksEncoder {

    private final byte[] result;
    private int currentOffset;

    public MetadataBlocksEncoder() {
        result = new byte[CONFIG.METADATA_BLOCK_SIZE_BYTES];
        currentOffset = 0;
    }

    public void writeInt(int value, int sizeInBytes) {
        for (int i = sizeInBytes-1 ; i >= 0 ; i -- ) {
            result[currentOffset+i] = (byte)(value%256);
            value /= 256;
        }
        currentOffset += sizeInBytes;
    }

    public void writeList(List<Integer> list, int maxLen, int elementSize) {
        for (Integer element : list) {
            writeInt(element, elementSize);
        }
        for (int i = list.size() ; i < maxLen ; i ++ ) {
            writeInt(0, elementSize);
        }
    }

    public void writeString(String s, int maxLen) {
        for (int i = 0 ; i < s.length() ; i ++ ) {
            writeInt(s.charAt(i), 1);
        }
        for (int i = s.length() ; i < maxLen ; i ++ ) {
            writeInt(0, 1);
        }
    }

    public byte[] getResult() {
        while (currentOffset < CONFIG.METADATA_BLOCK_SIZE_BYTES) {
            result[currentOffset++] = 0;
        }
        if (result.length != CONFIG.METADATA_BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException("Number of bytes doesn't match");
        }
        return result;
    }
}
