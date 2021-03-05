package model.adt;

import exception.HeapException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Heap<T2> implements IHeap<T2>{
    private HashMap<Integer, T2> heap;
    private Integer addr;

    public Heap() {
        heap = new HashMap<>();
        addr = 1;
    }

    @Override
    public Integer add(T2 val) {
        heap.put(addr, val);
        addr++;
        return addr-1;
    }

    @Override
    public T2 lookup(Integer addr) {
        if (!heap.containsKey(addr))
            throw new HeapException("Address doesn't exist");
        return heap.get(addr);
    }

    @Override
    public boolean isDefined(Integer addr) {
        return heap.containsKey(addr);
    }

    @Override
    public void update(Integer addr, T2 val) {
        if (!heap.containsKey(addr))
            throw new HeapException("Address doesn't exist");
        heap.replace(addr, val);
    }

    @Override
    public Set<Map.Entry<Integer, T2>> entrySet() {
        return heap.entrySet();
    }

    @Override
    public void setContent(Map<Integer, T2> h) {
        heap = (HashMap<Integer, T2>) h;
    }

    @Override
    public Map<Integer, T2> getContent() {
        return (Map<Integer, T2>)heap;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Integer key: heap.keySet()) {
            s.append(key.toString()).append(" --> ").append(heap.get(key)).append("\n");
        }
        return s.toString();
    }
}
