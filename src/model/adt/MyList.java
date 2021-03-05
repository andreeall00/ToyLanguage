package model.adt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MyList<T> implements MyIList<T> {
    private ArrayList<T> list;

    public MyList() {
        list = new ArrayList<>();
    }

    public MyList(List<T> l) {
        list = new ArrayList<>();
        list.addAll(l);
    }

    @Override
    public void append(T e) {
        list.add(e);
    }

    @Override
    public T pop() {
        return list.get(0);
    }

    @Override
    public boolean isEmpty() {
        return list.size()==0;
    }

    @Override
    public List<T> getContent() {
        return list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public boolean exists(T e) {
        return list.contains(e);
    }

    @Override
    public void remove(T e) {
        list.remove(e);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Object[] vals = list.toArray();
        for(Object val : vals)
            s.append(val.toString()).append("\n");
        return s.toString();
    }
}
