package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyILock;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStmt implements IStmt{
    private String id;
    private static Lock lock = new ReentrantLock();

    public LockStmt(String id) {
        this.id = id;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyILock lockTable = state.getLockTable();
        IntValue location = lockTable.getFreeLocation();
        lockTable.put(location, -1);
        lock.unlock();
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        if (symTable.lookup(id).getType().equals(new IntType()))
            symTable.update(id, location);
        else
            throw new ExecException("Lock Statement Execution: variable is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Lock Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "newLock(" + id + ")";
    }
}
