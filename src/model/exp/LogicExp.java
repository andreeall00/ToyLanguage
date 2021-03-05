package model.exp;

import exception.EvalException;
import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.BoolType;
import model.type.IType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

public class LogicExp implements IExp {
    private IExp e1;
    private IExp e2;
    private String op;

    public LogicExp(IExp e, IExp ex, String o) {
        e1 = e;
        e2 = ex;
        op = o;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, IHeap<IValue> heap) throws MyException {
        IValue v1, v2;
        v1 = e1.eval(tbl, heap);
        if(!v1.getType().equals(new BoolType()))
            throw new EvalException("Logic Expression Evaluation: first operand is not an boolean");
        v2 = e2.eval(tbl, heap);
        if(!v2.getType().equals(new BoolType()))
            throw new EvalException("Logic Expression Evaluation: second operand is not an boolean");
        boolean n1, n2;
        n1 = ((BoolValue)v1).getVal();
        n2 = ((BoolValue)v2).getVal();
        if(op.equals("&&"))
            return new BoolValue(n1&&n2);
        if(op.equals("||"))
            return new BoolValue(n1||n2);
        throw new EvalException("Logic Expression Evaluation: operation is not valid");
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ1 = e1.typeCheck(typeEnv);
        IType typ2 = e2.typeCheck(typeEnv);
        if (typ1.equals(new BoolType()))
            if (typ2.equals(new BoolType()))
                return new BoolType();
            else
                throw new MyException("Logic Expression TypeCheck: second operand is not an boolean");
        else
            throw new MyException("Logic Expression TypeCheck: first operand is not an boolean");
    }

    @Override
    public String toString() {
        return e1 + op + e2;
    }
}
