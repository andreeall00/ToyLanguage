package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.exp.IExp;
import model.type.IType;
import model.value.IValue;

public class PrintStmt implements IStmt {
    private IExp exp;

    public PrintStmt(IExp e) {
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<IValue> out = state.getOut();
        IHeap<IValue> heap = state.getHeap();
        out.append(exp.eval(state.getSymTable().top(), heap));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

    public String toString(){
        return "print(" + exp.toString() + ")";
    }
}
