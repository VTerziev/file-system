package jb.filesystem.accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleLocksProvider implements FileLocksProvider {
    private final List<Lock> locks;

    public SimpleLocksProvider(int maxFileCount) {
        locks = new ArrayList<>();
        for (int i = 0 ; i < maxFileCount; i ++ ) {
            locks.add(new ReentrantLock());
        }
    }

    @Override
    public void acquireLock(int fileId) {
        locks.get(fileId).lock();
    }

    @Override
    public void releaseLock(int fileId) {
        locks.get(fileId).unlock();
    }
}
