package jb.filesystem.blocks.traversing;

import java.util.List;

public class Traversor {
    public int getTotalLeavesCount(Traversable t) {
        int ret = t.getDirectLeavesCount();
        List<Traversable> children = t.getNonLeaves();
        if (!children.isEmpty()) {
            Traversable lastChild = getLastNonLeaf(t);
            ret += getTotalLeavesCount(lastChild);
            for (int i = 0 ; i +1 < children.size() ; i ++ ) {
                ret += children.get(i).getMaxLeafCapacity();
            }
        }
        return ret;
    }

    public int getLeaf (Traversable t, int leafOffset) {
        if (t.getDirectLeavesCount() > leafOffset) {
            return t.getDirectLeaf(leafOffset);
        }
        leafOffset -= t.getDirectLeavesCount();
        for (Traversable child : t.getNonLeaves()) {
            int countLeaves = getTotalLeavesCount(child);
            if (countLeaves > leafOffset) {
                return getLeaf(child, leafOffset);
            }
            leafOffset -= countLeaves;
        }
        throw new IllegalArgumentException("Leaf offset out of bounds");
    }

    public void appendLeaf(Traversable t, int leafId) {
        if (!tryAddLeaf(t, leafId)) {
            throw new IllegalStateException("No more slots for leaves");
        }
    }

    public void deleteLastLeaf(Traversable t) {
        List<Traversable> children = t.getNonLeaves();
        if (children.isEmpty()) {
            t.deleteLastDirectLeaf();
        } else {
            Traversable lastChild = getLastNonLeaf(t);
            deleteLastLeaf(lastChild);
            if (lastChild.getDirectLeavesCount() == 0 && lastChild.getNonLeaves().size() == 0) {
                t.deleteLastNonLeaf();
            }
        }
    }
    private boolean tryAddLeaf(Traversable t, int leafId) {
        if (t.hasDirectLeafSlotsAvailable()) {
            t.appendDirectLeaf(leafId);
            return true;
        }
        List<Traversable> children = t.getNonLeaves();
        if (children.isEmpty() || !tryAddLeaf(getLastNonLeaf(t), leafId)) {
            if (!t.nonLeafSlotsFull()) {
                t.createNewNonLeaf();
                return tryAddLeaf(getLastNonLeaf(t), leafId);
            } else {
                return false;
            }
        }
        return true;
    }

    private Traversable getLastNonLeaf(Traversable t) {
        return t.getNonLeaves().get(t.getNonLeaves().size()-1);
    }
}
