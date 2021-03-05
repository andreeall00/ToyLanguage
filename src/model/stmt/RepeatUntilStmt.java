package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.IExp;
import model.exp.NotExp;
import model.type.IType;

public class RepeatUntilStmt implements IStmt{
    private IStmt stmt;
    private IExp exp;

    public RepeatUntilStmt(IStmt stmt, IExp exp) {
        this.stmt = stmt;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        stk.push(new CompStmt(stmt, new WhileStmt(new NotExp(exp), stmt)));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat(" + stmt + ")until(" + exp + ")";
    }
}
