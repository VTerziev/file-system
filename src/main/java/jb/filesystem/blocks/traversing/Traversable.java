package jb.filesystem.blocks.traversing;

import java.util.List;

public interface Traversable { // TODO: remove MetadataManager from the Traversables
    int getId();
    boolean hasDirectLeafSlotsAvailable();
    void appendDirectLeaf(int leafId);
    int getDirectLeaf(int leafOffset);
    void deleteLastDirectLeaf();
    int getDirectLeavesCount();
    List<Traversable> getNonLeaves();
    void createNewNonLeaf();
    boolean nonLeafSlotsFull();
    void deleteLastNonLeaf();
    int getMaxLeafCapacity();
}
