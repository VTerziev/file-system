package jb.filesystem.blocks.traversing;

import java.util.List;

/**
 * A class, which represents a node of a tree. The node has a specific number of slots for children which are leaves
 * and children which are non-leaves.
 * Children of the tree are added in a specific manner: first, only direct
 * leaves are added. When all the leaf slots are full, then a non-leaf child is added. When that child is full, then
 * a next one is added, and so on.
 */
public interface TreeNode { // TODO: remove MetadataManager from the Node implementations
    int getId();
    boolean hasDirectLeafSlotsAvailable();
    void appendDirectLeaf(int leafId);
    int getDirectLeaf(int leafOffset);
    void deleteLastDirectLeaf();
    int getDirectLeavesCount();
    List<TreeNode> getNonLeaves();
    void createNewNonLeaf();
    boolean nonLeafSlotsFull();
    void deleteLastNonLeaf();
    int getMaxLeafCapacity();
}
