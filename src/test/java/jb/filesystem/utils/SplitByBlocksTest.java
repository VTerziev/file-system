package jb.filesystem.utils;

import org.junit.Assert;
import org.junit.Test;

public class SplitByBlocksTest {

    @Test
    public void startAfterEnd() {
        SplitByBlocks split = new SplitByBlocks(10, 10, 5);
        Assert.assertFalse(split.nextBlock());
    }

    @Test
    public void startAndEndInASingleBlock() {
        SplitByBlocks split = new SplitByBlocks(10, 5, 7);
        assertBlock(split, 5, 7, 0);
        Assert.assertFalse(split.nextBlock());
    }

    @Test
    public void intervalMatchesABlock() {
        SplitByBlocks split = new SplitByBlocks(10, 10, 20);
        assertBlock(split, 10, 20, 1);
        Assert.assertFalse(split.nextBlock());
    }

    @Test
    public void endInNextBlock() {
        SplitByBlocks split = new SplitByBlocks(10, 5, 15);
        assertBlock(split, 5, 10, 0);
        assertBlock(split, 10, 15, 1);
        Assert.assertFalse(split.nextBlock());
    }

    private void assertBlock(SplitByBlocks split, int expectedStart, int expectedEnd, int expectedId) {
        Assert.assertTrue(split.nextBlock());

        Assert.assertEquals(expectedStart, split.getCurrentBlockStart());
        Assert.assertEquals(expectedEnd, split.getCurrentBlockEnd());
        Assert.assertEquals(expectedId, split.getCurrentBlockId());

    }
}
