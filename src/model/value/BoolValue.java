package model.value;

import exception.MyException;
import model.type.BoolType;
import model.type.IType;

public class BoolValue implements IValue {
    boolean val;

    public BoolValue(boolean v) {
        val = v;
    }

    public boolean getVal() {
        return val;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(IValue other) throws MyException {
        if(!other.getType().equals(new BoolType()))
            throw new MyException("Different types");
        return val==((BoolValue)other).getVal();
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
