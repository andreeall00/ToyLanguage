package model.adt;

import java.util.stream.Stream;

public interface MyIStack<T> {
    T pop();
    void push(T v);
    boolean isEmpty();
    Stream<T> stream();
    T top();
}
