package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyILock;
import model.adt.MyIStack;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockLockStmt implements IStmt {
    private String id;
    private static Lock lock = new ReentrantLock();

    public LockLockStmt(String id) {
        this.id = id;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        IValue location = symTable.lookup(id);
        if (location.getType().equals(new IntType())) {
            lock.lock();
            MyILock lockTable = state.getLockTable();
            if (lockTable.isDefined((IntValue) location)) {
                if (lockTable.lookup((IntValue) location) == -1) {
                    lockTable.update((IntValue) location, state.getPersonalId());
                    lock.unlock();
                } else {
                    lock.unlock();
                    MyIStack<IStmt> stk = state.getExeStack();
                    stk.push(new LockLockStmt(id));
                }
            } else {
                lock.unlock();
                throw new ExecException("Lock Lock Statement Execution: invalid location");
            }
        } else
            throw new ExecException("Lock Lock Statement Execution: variable is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Lock Lock Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "lock(" + id + ")";
    }
}
