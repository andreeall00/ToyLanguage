package model.exp;

import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.IType;
import model.value.IValue;

public interface IExp {
    IValue eval(MyIDictionary<String, IValue>tbl, IHeap<IValue> heap) throws MyException;
    IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
