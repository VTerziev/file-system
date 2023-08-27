package jb.filesystem.utils;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SplitByBlocks { // TODO: make an iterator?
    private final int blockSize;
    private final int start;
    private final int end;
    private int currentStart;
    private int currentEnd;
    private int currentBlockId;
    private boolean isInitialized;

    public SplitByBlocks(int blockSize, int start, int end) {
        this.blockSize = blockSize;
        this.start = start;
        this.end = end;
        this.currentBlockId = start / blockSize;
        this.currentStart = -1;
        this.currentEnd = -1;
        this.isInitialized = false;
    }

    private void computeCurrent() {
        currentStart = max(this.start, currentBlockId*blockSize);
        currentEnd = min(this.end, (currentBlockId+1)*blockSize);
    }

    public boolean nextBlock() {
        if (isInitialized) {
            currentBlockId ++;
        } else {
            isInitialized = true;
        }
        computeCurrent();
        return currentStart < currentEnd;
    }

    public int getCurrentBlockStart() {
        return currentStart;
    }

    public int getCurrentBlockEnd() {
        return currentEnd;
    }

    public int getCurrentBlockId() {
        return currentBlockId;
    }
}
