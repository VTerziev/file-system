package jb.filesystem.metadata;

import java.util.List;

// TODO: add error checking
public class MetadataBlocksEncoder {

    private final byte[] result;
    private int currentOffset;

    public MetadataBlocksEncoder() {
        result = new byte[MetadataBlock.BLOCK_SIZE_BYTES];
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
        while (currentOffset < MetadataBlock.BLOCK_SIZE_BYTES) {
            result[currentOffset++] = 0;
        }
        return result;
    }
}
