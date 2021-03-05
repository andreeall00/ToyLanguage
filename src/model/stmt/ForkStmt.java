package model.stmt;

import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.*;
import model.type.IType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Iterator;


public class ForkStmt implements IStmt {
    private IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> newStack = new MyStack<>();

        MyIStack<MyIDictionary<String, IValue>> clonedSymTable = new MyStack<>();
        Iterator<MyIDictionary<String, IValue>> it = state.getSymTable().stream().iterator();
        while (it.hasNext()) {
            MyIDictionary<String, IValue> oldDict = it.next();
            MyIDictionary<String, IValue> newDict = new MyDictionary<>();
            for (String key: oldDict.getContent().keySet())
                newDict.put(key, oldDict.lookup(key));
            clonedSymTable.push(newDict);
        }
        return new PrgState(newStack, clonedSymTable, state.getOut(), stmt, state.getFileTable(), state.getHeap(),
                state.getSemaphoreTable(), state.getLatchTable(), state.getLockTable(), state.getBarrierTable(), state.getProcTable());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        MyIDictionary<String, IType> clonedTypeEnv = new MyDictionary<>();
        for (String key : typeEnv.getContent().keySet())
            clonedTypeEnv.put(key, typeEnv.lookup(key));
        stmt.typeCheck(clonedTypeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
