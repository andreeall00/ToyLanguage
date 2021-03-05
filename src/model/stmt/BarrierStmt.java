package model.stmt;

import exception.ExecException;
import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIBarrier;
import model.adt.MyIDictionary;
import model.adt.MyISemaphore;
import model.exp.IExp;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierStmt implements IStmt{
    private String id;
    private IExp exp;
    private static Lock lock = new ReentrantLock();

    public BarrierStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        IHeap<IValue> heap = state.getHeap();
        IValue val = exp.eval(symTable, heap);
        if (val.getType().equals(new IntType())) {
            lock.lock();
            MyIBarrier barrierTable = state.getBarrierTable();
            IntValue location = barrierTable.getFreeLocation();
            barrierTable.put(location, new Pair<>((IntValue) val, new ArrayList<>()));
            lock.unlock();
            if ((symTable.lookup(id)).getType().equals(new IntType())) {
                symTable.update(id, location);
            } else
                symTable.put(id, location);
        } else
            throw new ExecException("Barrier Statement Execution: expression evaluation is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType())) {
            if (exp.typeCheck(typeEnv).equals(new IntType())) {
                return typeEnv;
            } else
                throw new MyException("Barrier Statement TypeCheck: expression evaluation is not an integer ");
        } else
            throw new MyException("Barrier Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "barrier(" + id + "," + exp + ")";
    }
}
