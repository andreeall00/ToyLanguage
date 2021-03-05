package model.exp;

import exception.EvalException;
import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.BoolType;
import model.type.IType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.IValue;

public class RelationalExp implements IExp {
    private IExp e1;
    private IExp e2;
    private String op;

    public RelationalExp( String o, IExp e, IExp ex) {
        e1 = e;
        e2 = ex;
        op = o;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, IHeap<IValue> heap) throws MyException {
        IValue v1, v2;
        v1 = e1.eval(tbl, heap);
        if(!v1.getType().equals(new IntType()))
            throw new EvalException("Relational Expression Evaluation: first operand is not an int");
        v2 = e2.eval(tbl, heap);
        if(!v2.getType().equals(new IntType()))
            throw new EvalException("Relational Expression Evaluation: second operand is not an int");
        int n1, n2;
        n1 = ((IntValue)v1).getVal();
        n2 = ((IntValue)v2).getVal();
        if(op.equals("<"))
            return new BoolValue(n1<n2);
        if(op.equals("<="))
            return new BoolValue(n1<=n2);
        if(op.equals("=="))
            return new BoolValue(n1==n2);
        if(op.equals("!="))
            return new BoolValue(n1!=n2);
        if(op.equals(">"))
            return new BoolValue(n1>n2);
        if(op.equals(">="))
            return new BoolValue(n1>=n2);
        throw new EvalException("Relational Expression Evaluation: operation not valid");
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((e1.typeCheck(typeEnv)).equals(new IntType()))
            if ((e2.typeCheck(typeEnv)).equals(new IntType()))
                return new BoolType();
            else
                throw new MyException("Relational Expression TypeCheck: second operand is not an integer");
        else
            throw new MyException("Relational Expression TypeCheck: first operand is not an integer");
    }

    @Override
    public String toString() {
        return e1 + op + e2;
    }
}
