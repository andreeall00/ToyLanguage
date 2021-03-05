package model.exp;


import exception.EvalException;
import exception.MyException;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public class ReadHeapExp implements IExp{
    private IExp exp;

    public ReadHeapExp(IExp e) {
        exp = e;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue>tbl, IHeap<IValue> heap) throws MyException {
        IValue val = exp.eval(tbl, heap);
        if(val instanceof RefValue){
            if(heap.isDefined(((RefValue) val).getAddress()))
                return heap.lookup(((RefValue) val).getAddress());
            else
                throw new EvalException("Read Heap Expression Evaluation: address is not valid");
        }
        else
            throw new EvalException("Read Heap Expression Evaluation: value is not a reference");
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typ = exp.typeCheck(typeEnv);
        if (typ instanceof RefType){
            RefType reft = (RefType)typ;
            return reft.getInner();
        }
        else
            throw new MyException("Read Heap Expression TypeCheck: read heap argument is not a Ref Type");
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
}
