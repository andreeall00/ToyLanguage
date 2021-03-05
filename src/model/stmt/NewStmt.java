package model.stmt;


import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.exp.IExp;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public class NewStmt implements IStmt{
    private String varName;
    private IExp exp;

    public NewStmt(String vn, IExp e) {
        varName = vn;
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        IHeap<IValue> heap = state.getHeap();
        if (symTbl.isDefined(varName)) {
            if(symTbl.lookup(varName).getType() instanceof RefType) {
                IValue val = exp.eval(symTbl, heap);
                IType typeId = ((RefValue)symTbl.lookup(varName)).getLocationType();
                if (val.getType().equals(typeId)){
                    int addr = heap.add(val);
                    symTbl.update(varName, new RefValue(addr, val.getType()));
                }
                else
                    throw new ExecException("New Statement Execution: type of expression and type of variable do not match");
            }
            else
                throw new ExecException("New Statement Execution: variable is not a reference");
        }
        else
            throw new ExecException("New Statement Execution: variable is not declared");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(varName)).equals(new RefType(exp.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new MyException("New Statement TypeCheck: type of expression and type of variable do not match");
    }

    @Override
    public String toString() {
        return "new(" + varName + "," + exp.toString() +")";
    }
}
