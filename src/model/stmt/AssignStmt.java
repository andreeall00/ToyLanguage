package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.IExp;
import model.type.IType;
import model.value.IValue;

public class AssignStmt implements IStmt{
    private String id;
    private IExp exp;

    public AssignStmt(String i, IExp e) {
        id = i;
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        IHeap<IValue> heap = state.getHeap();
        if (symTbl.isDefined(id)) {
            IValue val = exp.eval(symTbl, heap);
            IType typeId = (symTbl.lookup(id)).getType();
            if (val.getType().equals(typeId))
                symTbl.update(id, val);
            else
                throw new ExecException("Assignment Statement Execution: type of expression and type of variable do not match");
        }
        else
            throw new ExecException("Assignment Statement Execution: variable id is not declared");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(exp.typeCheck(typeEnv)))
            return typeEnv;
        else
            throw new MyException("Assignment Statement TypeCheck: type of expression and type of variable do not match");
    };

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }
}
