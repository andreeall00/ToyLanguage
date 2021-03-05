package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyDictionary;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.IExp;
import model.type.BoolType;
import model.type.IType;
import model.value.BoolValue;
import model.value.IValue;

public class IfStmt implements IStmt {
    private IExp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(IExp e, IStmt t, IStmt el) {
        exp = e;
        thenS = t;
        elseS = el;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        IHeap<IValue> heap = state.getHeap();
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        if (exp.eval(symTbl, heap).equals(new BoolValue(true)))
            stk.push(thenS);
        else if (exp.eval(symTbl, heap).equals(new BoolValue(false)))
            stk.push(elseS);
        else throw new ExecException("If Statement Execution: no boolean evaluation for the if statement");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        MyIDictionary<String, IType> clonedTypeEnv1 = new MyDictionary<>();
        for (String key : typeEnv.getContent().keySet())
            clonedTypeEnv1.put(key, typeEnv.lookup(key));

        MyIDictionary<String, IType> clonedTypeEnv2 = new MyDictionary<>();
        for (String key : typeEnv.getContent().keySet())
            clonedTypeEnv2.put(key, typeEnv.lookup(key));

        if ((exp.typeCheck(typeEnv)).equals(new BoolType())) {
            thenS.typeCheck(clonedTypeEnv1);
            elseS.typeCheck(clonedTypeEnv2);
            return typeEnv;
        } else
            throw new MyException("If Statement TypeCheck: no boolean evaluation for the if statement");
    }

    @Override
    public String toString() {
        return "if(" + exp.toString() + ")then{" + thenS.toString() + "}else{" + elseS.toString() + "}";
    }
}
