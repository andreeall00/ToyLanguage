package model.adt;

import exception.LatchException;
import model.value.IntValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLatch implements MyILatch{
    private Map<IntValue, IntValue> latch;
    private static Lock lock = new ReentrantLock();
    private int freeLocation = 1;

    public MyLatch() {
        latch = new HashMap<>();
    }

    @Override
    public void put(IntValue id, IntValue val) throws LatchException {
        if (isDefined(id))
            throw new LatchException("Key already exists");
        lock.lock();
        latch.put(id, val);
        lock.unlock();
    }

    @Override
    public void remove(IntValue id) throws LatchException {
        if (!isDefined(id))
            throw new LatchException("Key doesn't exist");
        lock.lock();
        latch.remove(id);
        lock.unlock();
    }

    @Override
    public boolean isDefined(IntValue id) {
        lock.lock();
        boolean b = latch.containsKey(id);
        lock.unlock();
        return b;
    }

    @Override
    public IntValue lookup(IntValue id) throws LatchException {
        if (!isDefined(id))
            throw new LatchException("Key doesn't exist");
        lock.lock();
        IntValue rez =  latch.get(id);
        lock.unlock();
        return rez;
    }

    @Override
    public void update(IntValue id, IntValue val) throws LatchException {
        if (!isDefined(id))
            throw new LatchException("Key doesn't exist");
        lock.lock();
        latch.replace(id, val);
        lock.unlock();
    }

    @Override
    public Map<IntValue, IntValue> getContent() {
        lock.lock();
        Map<IntValue, IntValue> rez = latch;
        lock.unlock();
        return rez;
    }

    @Override
    public IntValue getFreeLocation() {
        lock.lock();
        IntValue rez = new IntValue(freeLocation);
        freeLocation++;
        lock.unlock();
        return rez;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (IntValue key: latch.keySet()) {
            s.append(key.toString()).append(" --> ").append(latch.get(key)).append("\n");
        }
        return s.toString();
    }
}
