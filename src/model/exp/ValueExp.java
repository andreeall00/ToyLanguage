package model.exp;

import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.IType;
import model.value.IValue;

public class ValueExp implements IExp{
    IValue val;

    public ValueExp(IValue v) {
        val = v;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue>tbl, IHeap<IValue> heap) throws MyException {
        return val;
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return val.getType();
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
