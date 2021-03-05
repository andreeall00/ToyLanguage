package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.IExp;
import model.type.BoolType;
import model.type.IType;

public class CondAssignStmt implements IStmt{
    private String id;
    private IExp exp1, exp2, exp3;

    public CondAssignStmt(String id, IExp exp1, IExp exp2, IExp exp3) {
        this.id = id;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        stk.push(new IfStmt(exp1, new AssignStmt(id, exp2), new AssignStmt(id, exp3)));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((exp1.typeCheck(typeEnv)).equals(new BoolType())) {
            if ((typeEnv.lookup(id)).equals(exp2.typeCheck(typeEnv)) && (exp2.typeCheck(typeEnv)).equals(exp3.typeCheck(typeEnv)))
                return typeEnv;
            else
                throw new MyException("Conditional Assignment Statement TypeCheck: type of expressions and type of variable do not match");
        } else
            throw new MyException("Conditional Assignment Statement TypeCheck: no boolean evaluation of the expression");
    }

    @Override
    public String toString() {
        return id + "=" + exp1 + "?" + exp2 + ":" + exp3;
    }
}
