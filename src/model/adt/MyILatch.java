package model.adt;

import exception.LatchException;
import model.value.IntValue;

import java.util.Map;

public interface MyILatch {
    void put(IntValue id, IntValue val) throws LatchException;
    void remove(IntValue id) throws LatchException;
    boolean isDefined(IntValue id);
    IntValue lookup(IntValue id) throws LatchException;
    void update(IntValue id, IntValue val) throws LatchException;
    Map<IntValue, IntValue> getContent();
    IntValue getFreeLocation();
}
