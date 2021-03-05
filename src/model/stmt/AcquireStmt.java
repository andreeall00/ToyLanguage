package model.stmt;

import exception.ExecException;
import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.*;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AcquireStmt implements IStmt {
    private String id;
    private static Lock lock = new ReentrantLock();

    public AcquireStmt(String id) {
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
                if (p.getKey().getVal() > p.getValue().size()) {
                    if (!p.getValue().contains(state.getPersonalId())) {
                        p.getValue().add(state.getPersonalId());
                    }
                } else {
                    MyIStack<IStmt> stk = state.getExeStack();
                    stk.push(new AcquireStmt(id));
                }
            } else
                throw new ExecException("Acquire Statement Execution: invalid location");
            lock.unlock();
        } else
            throw new ExecException("Acquire Statement Execution: variable is not an integer");
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
        return "acquire(" + id + ")";
    }
}
