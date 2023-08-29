package jb.filesystem.blocks.traversing;

import java.util.List;

/**
 * A class, which helps with traversing a tree, rooted at a TreeNode.
 * The leaves in the tree are added and removed in a specific order. Therefore, from a client's perspective,
 * it resembles a stack.
 */
public class Traversor {
    public int getTotalLeavesCount(TreeNode t) {
        int ret = t.getDirectLeavesCount();
        List<TreeNode> children = t.getNonLeaves();
        if (!children.isEmpty()) {
            TreeNode lastChild = getLastNonLeaf(t);
            ret += getTotalLeavesCount(lastChild);
            for (int i = 0 ; i +1 < children.size() ; i ++ ) {
                ret += children.get(i).getMaxLeafCapacity();
            }
        }
        return ret;
    }

    public int getLeaf (TreeNode t, int leafOffset) {
        if (t.getDirectLeavesCount() > leafOffset) {
            return t.getDirectLeaf(leafOffset);
        }
        leafOffset -= t.getDirectLeavesCount();
        for (TreeNode child : t.getNonLeaves()) {
            int countLeaves = getTotalLeavesCount(child);
            if (countLeaves > leafOffset) {
                return getLeaf(child, leafOffset);
            }
            leafOffset -= countLeaves;
        }
        throw new IllegalArgumentException("Leaf offset out of bounds");
    }

    public void appendLeaf(TreeNode t, int leafId) {
        if (!tryAddLeaf(t, leafId)) {
            throw new IllegalStateException("No more slots for leaves");
        }
    }

    public void deleteLastLeaf(TreeNode t) {
        List<TreeNode> children = t.getNonLeaves();
        if (children.isEmpty()) {
            t.deleteLastDirectLeaf();
        } else {
            TreeNode lastChild = getLastNonLeaf(t);
            deleteLastLeaf(lastChild);
            if (lastChild.getDirectLeavesCount() == 0 && lastChild.getNonLeaves().size() == 0) {
                t.deleteLastNonLeaf();
            }
        }
    }
    private boolean tryAddLeaf(TreeNode t, int leafId) {
        if (t.hasDirectLeafSlotsAvailable()) {
            t.appendDirectLeaf(leafId);
            return true;
        }
        List<TreeNode> children = t.getNonLeaves();
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

    private TreeNode getLastNonLeaf(TreeNode t) {
        return t.getNonLeaves().get(t.getNonLeaves().size()-1);
    }
}
