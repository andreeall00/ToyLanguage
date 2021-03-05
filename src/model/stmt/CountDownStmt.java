package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyILatch;
import model.adt.MyIList;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownStmt implements IStmt {
    private String id;
    private static Lock lock = new ReentrantLock();

    public CountDownStmt(String id) {
        this.id = id;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        IntValue location = (IntValue) symTable.lookup(id);
        if (location.getType().equals(new IntType())) {
            lock.lock();
            MyILatch latchTable = state.getLatchTable();
            if (latchTable.isDefined(location)) {
                int val = latchTable.lookup(location).getVal();
                if (val > 0) {
                    latchTable.update(location, new IntValue(val - 1));
                }
                lock.unlock();
                MyIList<IValue> out = state.getOut();
                out.append(new IntValue(state.getPersonalId()));
            } else {
                lock.unlock();
                throw new ExecException("Count Down Statement Execution: invalid location");
            }
        } else
            throw new ExecException("Count Down Statement Execution: variable is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Count Down Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "countDown(" + id + ")";
    }
}
