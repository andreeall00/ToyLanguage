package model.stmt;

import exception.ExecException;
import exception.LatchException;
import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.adt.MyILatch;
import model.exp.IExp;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LatchStmt implements IStmt{
    private String id;
    private IExp exp;
    private static Lock lock = new ReentrantLock();

    public LatchStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        IHeap<IValue> heap = state.getHeap();
        IValue eval = exp.eval(symTable, heap);
        if (eval.getType().equals(new IntType())) {
            lock.lock();
            MyILatch latchTable = state.getLatchTable();
            IntValue location = latchTable.getFreeLocation();
            latchTable.put(location, (IntValue)eval);
            lock.unlock();
            if ((symTable.lookup(id)).getType().equals(new IntType())) {
                symTable.update(id, location);
            } else
                throw new ExecException("Latch Statement Execution: variable is not an integer");
        }
        else
            throw new LatchException("Latch Statement Execution: expression evaluation is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType())) {
            if (exp.typeCheck(typeEnv).equals(new IntType())) {
                return typeEnv;
            } else
                throw new MyException("Latch Statement TypeCheck: expression evaluation is not an integer ");
        } else
            throw new MyException("Latch Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "latch(" + id + "," + exp + ")";
    }
}
