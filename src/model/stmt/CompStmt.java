package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.type.IType;

public class CompStmt implements IStmt{
    private IStmt first;
    private IStmt snd;

    public CompStmt(IStmt f, IStmt s) {
        first = f;
        snd = s;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk=state.getExeStack();
        stk.push(snd);
        stk.push(first);
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return snd.typeCheck(first.typeCheck(typeEnv));
    }

    @Override
    public String toString() {
        return first.toString() + "; " + snd.toString();
    }
}
