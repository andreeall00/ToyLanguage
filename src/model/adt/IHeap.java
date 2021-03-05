package model.adt;

import java.util.Map;
import java.util.Set;

public interface IHeap<T2> {
    Integer add(T2 val);
    T2 lookup(Integer addr);
    boolean isDefined(Integer addr);
    void update(Integer addr, T2 val);
    Set<Map.Entry<Integer, T2>> entrySet();
    void setContent(Map<Integer, T2> h);
    Map<Integer, T2> getContent();
}
