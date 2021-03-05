package model.adt;

import java.util.List;
import java.util.stream.Stream;

public interface MyIList<T> {
    void append(T e);
    T pop();
    boolean isEmpty();
    List<T> getContent();
    int size();
    Stream<T> stream();
    boolean exists(T e);
    void remove(T e);
}
