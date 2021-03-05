package model.adt;

import exception.DictionaryException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<T1, T2> implements MyIDictionary<T1, T2> {
    private HashMap<T1, T2> dict;

    public MyDictionary() {
        dict = new HashMap<>();
    }

    @Override
    public void put(T1 id, T2 val) throws DictionaryException {
        if (dict.containsKey(id))
            throw new DictionaryException("Key already exists");
        dict.put(id, val);
    }

    @Override
    public void remove(T1 id) throws DictionaryException {
        if (!dict.containsKey(id))
            throw new DictionaryException("Key doesn't exist");
        dict.remove(id);
    }

    @Override
    public boolean isDefined(T1 id) {
        return dict.containsKey(id);
    }

    @Override
    public T2 lookup(T1 id) throws DictionaryException {
        if (!dict.containsKey(id))
            throw new DictionaryException("Key doesn't exist");
        return dict.get(id);
    }

    @Override
    public void update(T1 id, T2 val) throws DictionaryException {
        if (!dict.containsKey(id))
            throw new DictionaryException("Key doesn't exist");
        dict.replace(id, val);
    }

    @Override
    public Map<T1, T2> getContent() {
        return (Map<T1,T2>)dict;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (T1 key: dict.keySet()) {
            s.append(key.toString()).append(" --> ").append(dict.get(key)).append("\n");
        }
        return s.toString();
    }
}
