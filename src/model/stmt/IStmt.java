package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.type.IType;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
