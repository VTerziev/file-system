package jb.filesystem.utils;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * A class, which splits an interval (for example, from 2 to 12) into several smaller intervals, breaking at regular
 * intervals. For example, we can split the interval [5, 35), breaking at every multiple of 10. This would result in
 * 4 sub-intervals: [5, 10), [10, 20), [20, 30), [30, 35).
 */
public class SplitByBlocks {
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
