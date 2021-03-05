package model.stmt;

import exception.EvalException;
import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.exp.IExp;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public class WriteHeapStmt implements IStmt {
    private String varName;
    private IExp exp;

    public WriteHeapStmt(String vn, IExp e) {
        varName = vn;
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        IHeap<IValue> heap = state.getHeap();
        if (symTbl.isDefined(varName)) {
            IValue var = symTbl.lookup(varName);
            if (var.getType() instanceof RefType) {
                if (heap.isDefined(((RefValue) var).getAddress())) {
                    IValue val = exp.eval(symTbl, heap);
                    if (val.getType().equals(((RefValue) var).getLocationType()))
                        heap.update(((RefValue) var).getAddress(), val);
                    else
                        throw new EvalException("Write Heap Statement Execution: type of expression and type of variable do not match");
                } else
                    throw new EvalException("Write Heap Statement Execution: address is not valid");
            } else
                throw new EvalException("Write Heap Statement Execution: variable is not a reference");
        } else
            throw new EvalException("Write Heap Statement Execution: variable is not defined");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(varName)).equals(new RefType(exp.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new MyException("Write Heap Statement TypeCheck: type of expression and type of variable do not match");
    }

    @Override
    public String toString() {
        return "wH(" + varName + "," + exp.toString() + ")";
    }
}
