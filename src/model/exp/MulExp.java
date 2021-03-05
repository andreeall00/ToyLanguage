package model.exp;

import exception.EvalException;
import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

public class MulExp implements IExp{
    private IExp e1, e2;

    public MulExp(IExp e1, IExp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, IHeap<IValue> heap) throws MyException {
        IValue v1, v2;
        v1 = e1.eval(tbl, heap);
        if(!v1.getType().equals(new IntType()))
            throw new EvalException("Mul Expression Evaluation: first operand is not an integer");
        v2 = e2.eval(tbl, heap);
        if(!v2.getType().equals(new IntType()))
            throw new EvalException("Mul Expression Evaluation: second operand is not an integer");
        int n1, n2;
        n1 = ((IntValue) v1).getVal();
        n2 = ((IntValue)v2).getVal();
        return new IntValue((n1*n2)-(n1+n2));
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ1, typ2;
        typ1 = e1.typeCheck(typeEnv);
        typ2 = e2.typeCheck(typeEnv);
        if (typ1.equals(new IntType()))
            if (typ2.equals(new IntType()))
                return new IntType();
            else
                throw new MyException("Mul Expression TypeCheck: second operand is not an integer");
        else
            throw new MyException("Mul Expression TypeCheck: first operand is not an integer");
    }

    @Override
    public String toString() {
        return "mul(" + e1 + "," + e2 + ")";
    }
}
