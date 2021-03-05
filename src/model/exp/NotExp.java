package model.exp;

import exception.EvalException;
import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.BoolType;
import model.type.IType;
import model.value.BoolValue;
import model.value.IValue;

public class NotExp implements IExp{
    private IExp exp;

    public NotExp(IExp exp) {
        this.exp = exp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> tbl, IHeap<IValue> heap) throws MyException {
        IValue val = exp.eval(tbl, heap);
        if (val.getType().equals(new BoolType())) {
            return new BoolValue(!(((BoolValue) val).getVal()));
        }
        else
            throw new EvalException("Not Expression Evaluation: expression evaluation is not a boolean");
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (exp.typeCheck(typeEnv).equals(new BoolType()))
            return new BoolType();
        else
            throw new EvalException("Not Expression TypeCheck: expression evaluation is not a boolean");
    }

    @Override
    public String toString() {
        return "!" + exp;
    }
}
