package model.adt;

import exception.BarrierException;
import javafx.util.Pair;
import model.value.IntValue;

import java.util.List;
import java.util.Map;

public interface MyIBarrier {
    void put(IntValue id, Pair<IntValue, List<Integer>> val) throws BarrierException;
    void remove(IntValue id) throws BarrierException;
    boolean isDefined(IntValue id);
    Pair<IntValue, List<Integer>> lookup(IntValue id) throws BarrierException;
    void update(IntValue id, Pair<IntValue, List<Integer>> val) throws BarrierException;
    Map<IntValue, Pair<IntValue, List<Integer>>> getContent();
    IntValue getFreeLocation();
}
