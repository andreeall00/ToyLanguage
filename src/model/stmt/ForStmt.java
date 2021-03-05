package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.IExp;
import model.exp.RelationalExp;
import model.exp.VarExp;
import model.type.IType;
import model.type.IntType;


public class ForStmt implements IStmt{
    private String id;
    private IExp exp1, exp2, exp3;
    private IStmt stmt;

    public ForStmt(String id, IExp exp1, IExp exp2, IExp exp3, IStmt stmt) {
        this.id = id;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        stk.push(new CompStmt(new VarDeclStmt(id, new IntType()), new CompStmt(new AssignStmt(id, exp1),
                new WhileStmt(new RelationalExp("<", new VarExp(id), exp2), new CompStmt(stmt, new AssignStmt(id, exp3))))));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        typeEnv.put(id, new IntType());
        if (exp1.typeCheck(typeEnv).equals(new IntType()) && exp2.typeCheck(typeEnv).equals(new IntType())
                && exp3.typeCheck(typeEnv).equals(new IntType())) {
            return typeEnv;
        }
        else
            throw new MyException("For Statement TypeCheck: no integer evaluation");
    }

    @Override
    public String toString() {
        return "for(" + id + "=" + exp1 + ";" + id + "<" + exp2 + ";" + id + "=" + exp3 + ")" + stmt;
    }
}
