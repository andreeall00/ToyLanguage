package model.value;

import exception.MyException;
import model.type.IType;

public interface IValue {
    IType getType();
    boolean equals(IValue other) throws MyException;
}
