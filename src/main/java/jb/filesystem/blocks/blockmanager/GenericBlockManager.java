package jb.filesystem.blocks.blockmanager;

public interface GenericBlockManager<T> {
    T getBlock(int blockId);

    void saveBlock(int blockId, T block);

    int allocateBlock();

    void deallocateBlock(int blockId);
}
