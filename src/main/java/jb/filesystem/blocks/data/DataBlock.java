package jb.filesystem.blocks.data;

/**
 * A block, which stores a chunk of the data of some file.
 */
public class DataBlock {
    private final byte[] bytes;
    public DataBlock(byte[] bytes) {
        this.bytes = bytes;
    }

    public void write(int offset, int l, int r, byte[] bytesToWrite) {
        if (r - l >= 0) System.arraycopy(bytesToWrite, l, bytes, offset, r - l);
    }

    public void read(int offset, int l, int r, byte[] buffer) {
        if (r - l >= 0) System.arraycopy(bytes, offset, buffer, l, r - l);
    }

    public byte[] getBytes() {
        return bytes;
    }
}
