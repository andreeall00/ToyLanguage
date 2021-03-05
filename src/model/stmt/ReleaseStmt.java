package model.stmt;

import exception.ExecException;
import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIList;
import model.adt.MyISemaphore;
import model.adt.MyIStack;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseStmt implements IStmt {
    private String id;
    private static Lock lock = new ReentrantLock();

    public ReleaseStmt(String id) {
        this.id = id;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        if ((symTable.lookup(id)).getType().equals(new IntType())) {
            IntValue index = (IntValue) symTable.lookup(id);
            lock.lock();
            MyISemaphore semTable = state.getSemaphoreTable();
            if (semTable.isDefined(index)) {
                Pair<IntValue, List<Integer>> p = semTable.lookup(index);
                if (p.getValue().contains(state.getPersonalId())) {
                    p.getValue().remove((Integer) state.getPersonalId());
                }
            } else
                throw new ExecException("Release Statement Execution: invalid location");
        lock.unlock();
        } else
            throw new ExecException("Release Statement Execution: variable is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Acquire Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "release(" + id + ")";
    }
}
