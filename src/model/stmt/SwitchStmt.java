package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.MyDictionary;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.exp.IExp;
import model.exp.RelationalExp;
import model.type.IType;

public class SwitchStmt implements IStmt {
    private IExp exp, exp1, exp2;
    private IStmt stmt1, stmt2, stmt3;

    public SwitchStmt(IExp exp, IExp exp1, IStmt stmt1, IExp exp2, IStmt stmt2, IStmt stmt3) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
        this.stmt3 = stmt3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        stk.push(new IfStmt(new RelationalExp("==", exp, exp1), stmt1, new IfStmt(new RelationalExp("==", exp,
                exp2), stmt2, stmt3)));
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

        MyIDictionary<String, IType> clonedTypeEnv3 = new MyDictionary<>();
        for (String key : typeEnv.getContent().keySet())
            clonedTypeEnv3.put(key, typeEnv.lookup(key));

        if ((exp.typeCheck(typeEnv)).equals(exp1.typeCheck(typeEnv)) && (exp1.typeCheck(typeEnv)).equals(exp2.typeCheck(typeEnv))) {
            stmt1.typeCheck(clonedTypeEnv1);
            stmt2.typeCheck(clonedTypeEnv2);
            stmt3.typeCheck(clonedTypeEnv3);
            return typeEnv;
        } else
            throw new MyException("Switch Statement TypeCheck: the expressions have different types");
    }

    @Override
    public String toString() {
        return "switch(" + exp + ")(case(" + exp1 + "):" + stmt1 + ")(case(" + exp2 + "):" + stmt2 + ")(default:" + stmt3 + ")";
    }
}
