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

public class WhileStmt implements IStmt{
    private IExp exp;
    private IStmt stmt;

    public WhileStmt(IExp e, IStmt s){
        exp = e;
        stmt = s;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        IHeap<IValue> heap = state.getHeap();
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        if (exp.eval(symTbl, heap).equals(new BoolValue(true))) {
            stk.push(new WhileStmt(exp,stmt));
            stk.push(stmt);
        }
        else if (exp.eval(symTbl, heap).equals(new BoolValue(false)))
            return null;
        else throw new ExecException("While Statement Execution: no boolean evaluation for the while statement");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        MyIDictionary<String, IType> clonedTypeEnv = new MyDictionary<>();
        for (String key : typeEnv.getContent().keySet())
            clonedTypeEnv.put(key, typeEnv.lookup(key));
        if ((exp.typeCheck(typeEnv)).equals(new BoolType())){
            stmt.typeCheck(clonedTypeEnv);
            return typeEnv;
        }
        else
            throw new MyException("While Statement TypeCheck: no boolean evaluation for the while statement");
    }

    @Override
    public String toString() {
        return "while(" + exp.toString() + "){" + stmt.toString() + "}";
    }
}
