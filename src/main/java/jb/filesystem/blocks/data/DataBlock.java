package jb.filesystem.blocks.data;

public class DataBlock {
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
