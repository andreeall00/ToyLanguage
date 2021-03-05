package model.adt;

import exception.DictionaryException;

import java.util.Map;

public interface MyIDictionary<T1, T2> {
    void put(T1 id, T2 val) throws DictionaryException;
    void remove(T1 id) throws DictionaryException;
    boolean isDefined(T1 id);
    T2 lookup(T1 id) throws DictionaryException;
    void update(T1 id, T2 val) throws DictionaryException;
    Map<T1, T2> getContent();
}
