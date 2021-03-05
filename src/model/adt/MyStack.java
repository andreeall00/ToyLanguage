package model.adt;

import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.stream.Stream;

public class MyStack<T> implements MyIStack<T>{
    private Stack<T> stack;

    public MyStack() {
        stack = new Stack<>();
    }

    @Override
    public T pop() {
        return stack.pop();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Stream<T> stream() {
        return stack.stream();
    }

    @Override
    public T top() {
        return stack.peek();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Object[] vals = stack.toArray();
        for (int i = vals.length - 1; i >= 0; i -= 1)
            s.append(vals[i].toString()).append("\n");
        return s.toString();
    }
}
