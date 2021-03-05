package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.*;
import model.exp.IExp;
import model.type.IType;
import model.value.IValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallStmt implements IStmt {
    private String id;
    private MyIList<IExp> exps;

    public CallStmt(String id, MyIList<IExp> exps) {
        this.id = id;
        this.exps = exps;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIProc procTable = state.getProcTable();
        if (procTable.isDefined(id)) {
            List<String> vars = procTable.lookup(id).getKey();
            IStmt stmt = procTable.lookup(id).getValue();
            MyIStack<MyIDictionary<String, IValue>> symTable = state.getSymTable();
            List<IValue> vals = new ArrayList<>();
            exps.getContent().forEach(e -> vals.add(e.eval(symTable.top(), state.getHeap())));
            if (vals.size() == vars.size()) {
                MyIDictionary<String, IValue> newSymTable = new MyDictionary<>();
                Iterator<String> varIt = vars.iterator();
                Iterator<IValue> valIt = vals.iterator();
                while (varIt.hasNext())
                    newSymTable.put(varIt.next(), valIt.next());
                symTable.push(newSymTable);
                MyIStack<IStmt> stk = state.getExeStack();
                stk.push(new ReturnStmt());
                stk.push(stmt);
            } else
                throw new ExecException("Call Statement Execution: the number of arguments is not equal to the number of parameters");
        } else
            throw new ExecException("Call Statement Execution: the procedure is not defined");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "call(" + id + ":" + exps.getContent() + ")";
    }

    private static class ReturnStmt implements IStmt {
        @Override
        public PrgState execute(PrgState state) throws MyException {
            state.getSymTable().pop();
            return null;
        }

        @Override
        public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
            return typeEnv;
        }

        @Override
        public String toString() {
            return "return";
        }
    }
}
