package model.stmt;

import exception.ExecException;
import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.MyIBarrier;
import model.adt.MyIDictionary;
import model.adt.MyILatch;
import model.adt.MyIStack;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitBStmt implements IStmt{
    private String id;
    private static Lock lock = new ReentrantLock();

    public AwaitBStmt(String id) {
        this.id = id;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        IntValue location = (IntValue) symTable.lookup(id);
        if (location.getType().equals(new IntType())) {
            lock.lock();
            MyIBarrier barrierTable = state.getBarrierTable();
            if (barrierTable.isDefined(location)) {
                Pair<IntValue, List<Integer>> val = barrierTable.lookup(location);
                if (val.getKey().getVal() > val.getValue().size()) {
                    if (!val.getValue().contains(state.getPersonalId()))
                        val.getValue().add(state.getPersonalId());
                    MyIStack<IStmt> stk = state.getExeStack();
                    stk.push(new AwaitBStmt(id));
                }
                lock.unlock();
            } else {
                lock.unlock();
                throw new ExecException("Await Statement Execution: invalid location");
            }
        } else
            throw new ExecException("Await Statement Execution: variable is not an integer");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((typeEnv.lookup(id)).equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("Await Statement TypeCheck: variable is not an integer");
    }

    @Override
    public String toString() {
        return "await(" + id + ")";
    }
}
