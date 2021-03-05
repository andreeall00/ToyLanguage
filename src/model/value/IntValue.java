package model.value;

import exception.MyException;
import model.type.IType;
import model.type.IntType;

public class IntValue implements IValue {
    private int val;

    public IntValue(int v) {
        val = v;
    }

    public int getVal() {
        return val;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public boolean equals(IValue other) throws MyException {
        if(!other.getType().equals(new IntType()))
            throw new MyException("Different types");
        return val==((IntValue)other).getVal();
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
