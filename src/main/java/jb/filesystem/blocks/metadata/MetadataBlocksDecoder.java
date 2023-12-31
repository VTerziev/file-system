package jb.filesystem.blocks.metadata;

import java.util.ArrayList;
import java.util.List;

import static jb.filesystem.config.FileSystemConfig.CONFIG;

/**
 * A helper class, which supports decoding a byte array into a metadata block.
 */
public class MetadataBlocksDecoder {

    private final byte[] bytes;
    private int offset;

    public MetadataBlocksDecoder(byte[] bytes) {
        if (bytes.length != CONFIG.METADATA_BLOCK_SIZE_BYTES) {
            throw new IllegalArgumentException("Number of bytes doesn't match");
        }
        this.bytes = bytes;
        this.offset = 0;
    }

    public String readString(int maxLen) {
        int len = maxLen;
        for (int i = 0; i < maxLen; i++) {
            if (bytes[offset + i] == 0) {
                len = i;
                break;
            }
        }
        offset += maxLen;
        return new String(bytes, offset-maxLen, len);
    }

    public List<Integer> readList(int maxLen, int elementSize) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < maxLen; i ++) {
            int element = readInteger(elementSize);
            if (element != 0) {
                result.add(element);
            }
        }
        return result;
    }

    public int readInteger(int numberOfBytes) {
        int result = 0;
        for (int i = offset;  i < offset + numberOfBytes ; i ++ ) {
            result <<= 8;
            result += fromByte(bytes[i]);
        }
        offset += numberOfBytes;
        return result;
    }

    private int fromByte(byte b) {
        return b < 0 ? b + 256 : b;
    }
}
