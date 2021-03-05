package model.adt;

import exception.BarrierException;
import javafx.util.Pair;
import model.value.IntValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBarrier implements MyIBarrier{
    private Map<IntValue, Pair<IntValue, List<Integer>>> barrier;
    private static Lock lock = new ReentrantLock();
    private int freeLocation = 1;

    public MyBarrier() {
        barrier = new HashMap<>();
    }

    @Override
    public void put(IntValue id, Pair<IntValue, List<Integer>> val) throws BarrierException {
        if (isDefined(id))
            throw new BarrierException("Key already exists");
        lock.lock();
        barrier.put(id, val);
        lock.unlock();
    }

    @Override
    public void remove(IntValue id) throws BarrierException {
        if (!isDefined(id))
            throw new BarrierException("Key doesn't exist");
        lock.lock();
        barrier.remove(id);
        lock.unlock();
    }

    @Override
    public boolean isDefined(IntValue id) {
        lock.lock();
        boolean b = barrier.containsKey(id);
        lock.unlock();
        return b;
    }

    @Override
    public Pair<IntValue, List<Integer>> lookup(IntValue id) throws BarrierException {
        if (!isDefined(id))
            throw new BarrierException("Key doesn't exist");
        lock.lock();
        Pair<IntValue, List<Integer>> rez =  barrier.get(id);
        lock.unlock();
        return rez;
    }

    @Override
    public void update(IntValue id, Pair<IntValue, List<Integer>> val) throws BarrierException {
        if (!isDefined(id))
            throw new BarrierException("Key doesn't exist");
        lock.lock();
        barrier.replace(id, val);
        lock.unlock();
    }

    @Override
    public Map<IntValue, Pair<IntValue, List<Integer>>> getContent() {
        lock.lock();
        Map<IntValue, Pair<IntValue, List<Integer>>> rez = barrier;
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
        for (IntValue key: barrier.keySet()) {
            s.append(key.toString()).append(" --> ").append(barrier.get(key).getKey()).append(" -> ").append(barrier.get(key).getValue()).append("\n");
        }
        return s.toString();
    }
}
