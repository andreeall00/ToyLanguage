package model.adt;


import exception.LockException;
import model.value.IntValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLock implements MyILock{
    private Map<IntValue, Integer> myLock;
    private static Lock lock = new ReentrantLock();
    private int freeLocation = 1;

    public MyLock() {
        myLock = new HashMap<>();
    }

    @Override
    public void put(IntValue id, Integer val) throws LockException {
        if (isDefined(id))
            throw new LockException("Key already exists");
        lock.lock();
        myLock.put(id, val);
        lock.unlock();
    }

    @Override
    public void remove(IntValue id) throws LockException {
        if (!isDefined(id))
            throw new LockException("Key doesn't exist");
        lock.lock();
        myLock.remove(id);
        lock.unlock();
    }

    @Override
    public boolean isDefined(IntValue id) {
        lock.lock();
        boolean b = myLock.containsKey(id);
        lock.unlock();
        return b;
    }

    @Override
    public Integer lookup(IntValue id) throws LockException {
        if (!isDefined(id))
            throw new LockException("Key doesn't exist");
        lock.lock();
        Integer rez =  myLock.get(id);
        lock.unlock();
        return rez;
    }

    @Override
    public void update(IntValue id, Integer val) throws LockException {
        if (!isDefined(id))
            throw new LockException("Key doesn't exist");
        lock.lock();
        myLock.replace(id, val);
        lock.unlock();
    }

    @Override
    public Map<IntValue, Integer> getContent() {
        lock.lock();
        Map<IntValue, Integer> rez = myLock;
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
        for (IntValue key: myLock.keySet()) {
            s.append(key.toString()).append(" --> ").append(myLock.get(key)).append("\n");
        }
        return s.toString();
    }
}
