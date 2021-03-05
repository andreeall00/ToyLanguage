package model.adt;

import exception.LockException;
import model.value.IntValue;

import java.util.Map;

public interface MyILock {
    void put(IntValue id, Integer val) throws LockException;
    void remove(IntValue id) throws LockException;
    boolean isDefined(IntValue id);
    Integer lookup(IntValue id) throws LockException;
    void update(IntValue id, Integer val) throws LockException;
    Map<IntValue, Integer> getContent();
    IntValue getFreeLocation();
}
