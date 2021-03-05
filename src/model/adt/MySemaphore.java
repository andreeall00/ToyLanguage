package model.adt;

import exception.SemaphoreException;
import javafx.util.Pair;
import model.value.IntValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MySemaphore implements MyISemaphore{
    private Map<IntValue, Pair<IntValue, List<Integer>>> semaphore;
    private static Lock lock = new ReentrantLock();
    private int freeLocation = 1;

    public MySemaphore() {
        semaphore = new HashMap<>();
    }

    @Override
    public void put(IntValue id, Pair<IntValue, List<Integer>> val) throws SemaphoreException {
        if (isDefined(id))
            throw new SemaphoreException("Key already exists");
        lock.lock();
        semaphore.put(id, val);
        lock.unlock();
    }

    @Override
    public void remove(IntValue id) throws SemaphoreException {
        if (!isDefined(id))
            throw new SemaphoreException("Key doesn't exist");
        lock.lock();
        semaphore.remove(id);
        lock.unlock();
    }

    @Override
    public boolean isDefined(IntValue id) {
        lock.lock();
        boolean b = semaphore.containsKey(id);
        lock.unlock();
        return b;
    }

    @Override
    public Pair<IntValue, List<Integer>> lookup(IntValue id) throws SemaphoreException {
        if (!isDefined(id))
            throw new SemaphoreException("Key doesn't exist");
        lock.lock();
        Pair<IntValue, List<Integer>> rez =  semaphore.get(id);
        lock.unlock();
        return rez;
    }

    @Override
    public void update(IntValue id, Pair<IntValue, List<Integer>> val) throws SemaphoreException {
        if (!isDefined(id))
            throw new SemaphoreException("Key doesn't exist");
        lock.lock();
        semaphore.replace(id, val);
        lock.unlock();
    }

    @Override
    public Map<IntValue, Pair<IntValue, List<Integer>>> getContent() {
        lock.lock();
        Map<IntValue, Pair<IntValue, List<Integer>>> rez = semaphore;
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
        for (IntValue key: semaphore.keySet()) {
            s.append(key.toString()).append(" --> ").append(semaphore.get(key).getKey()).append(" -> ").append(semaphore.get(key).getValue()).append("\n");
        }
        return s.toString();
    }
}
