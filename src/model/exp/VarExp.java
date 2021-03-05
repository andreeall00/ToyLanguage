package model.exp;

import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.IType;
import model.value.IValue;

public class VarExp implements IExp {
    String id;

    public VarExp(String i) {
        id = i;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, IHeap<IValue> heap) throws MyException {
        return tbl.lookup(id);
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
