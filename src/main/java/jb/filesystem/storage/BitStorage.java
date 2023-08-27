package jb.filesystem.storage;

public class BitStorage {

    private final ByteStorage storage;
    public BitStorage(ByteStorage storage) {
        this.storage = storage;
    }

    public boolean writeBit(int index, boolean value) {
        byte[] bytes = new byte[1];
        int x = storage.read(index/8, 1, bytes);
        if (x != 1 ) {
            throw new IllegalStateException("Couldn't read bit");
        }
        bytes[0] = setBitTo(bytes[0], index%8, value);
        x = storage.write(index/8, 1, bytes);
        return x > 0;
    }

    public boolean readBit(int index) {
        byte[] bytes = new byte[1];
        int x = storage.read(index/8, 1, bytes);
        if (x != 1 ) {
            throw new IllegalStateException("Couldn't read bit");
        }
        return hasBitActive(bytes[0], index%8);
    }

    private byte setBitTo(byte b, int bitIdx, boolean value) {
        if (value) {
            return (byte) (b|(getSingle1Bitmask(bitIdx)));
        } else {
            return (byte) (b&(getSingle0Bitmask(bitIdx)));
        }
    }

    private boolean hasBitActive(byte b, int bitIdx) {
        return (b&getSingle1Bitmask(bitIdx)) > 0;
    }

    private byte getSingle1Bitmask(int bitIdx) {
        return (byte) (1<<bitIdx);
    }

    private byte getSingle0Bitmask(int bitIdx) {
        return (byte) (~getSingle1Bitmask(bitIdx));
    }
}