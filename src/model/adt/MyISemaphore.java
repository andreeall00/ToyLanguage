package model.adt;

import exception.SemaphoreException;
import javafx.util.Pair;
import model.value.IntValue;

import java.util.List;
import java.util.Map;

public interface MyISemaphore {
    void put(IntValue id, Pair<IntValue, List<Integer>> val) throws SemaphoreException;
    void remove(IntValue id) throws SemaphoreException;
    boolean isDefined(IntValue id);
    Pair<IntValue, List<Integer>> lookup(IntValue id) throws SemaphoreException;
    void update(IntValue id, Pair<IntValue, List<Integer>> val) throws SemaphoreException;
    Map<IntValue, Pair<IntValue, List<Integer>>> getContent();
    IntValue getFreeLocation();
}
