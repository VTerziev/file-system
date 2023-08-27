package jb.filesystem.data;

public class DataBlock {
    public static final int BLOCK_SIZE_BYTES = 16;// TODO: do I need this
    public static final int DATA_BLOCK_POINTER_SIZE_BYTES = 2; // TODO: should this be defined elsewhere?

    private byte[] bytes;
    public DataBlock(byte[] bytes) {
        this.bytes = bytes;
    }

    public void write(int offset, int l, int r, byte[] bytesToWrite) { // TODO: return something?
        for (int i = l ; i < r ; i ++ ) {
            bytes[i-l+offset] = bytesToWrite[i];
        }
    }

    public void read(int offset, int l, int r, byte[] buffer) { // TODO: return something?
        for (int i = l ; i < r ; i ++ ) {
            buffer[i] = bytes[i-l+offset];
        }
    }

    public byte[] getBytes() {
        return bytes;
    }
}
