package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.ArithExp;
import model.exp.IExp;
import model.exp.ValueExp;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

public class WaitStmt implements IStmt{
    private IExp exp;

    public WaitStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        IHeap<IValue> heap = state.getHeap();
        MyIStack<IStmt> stk = state.getExeStack();
        IValue val = exp.eval(symTbl, heap);
        if (val.getType().equals(new IntType())) {
            if (((IntValue) val).getVal() > 0) {
                stk.push(new CompStmt(new PrintStmt(exp), new WaitStmt(new ArithExp("-", exp, new ValueExp(
                        new IntValue(1))))));
            }
        }
        else
            throw new ExecException("Wait Statement Execution: expression evaluation is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (exp.typeCheck(typeEnv).equals(new IntType())) {
            return typeEnv;
        }
        else
            throw new ExecException("Wait Statement TypeCheck: expression evaluation is not an integer");
    }

    @Override
    public String toString() {
        return "wait(" + exp + ")";
    }
}
