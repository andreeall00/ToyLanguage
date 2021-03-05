package model.stmt;

import exception.MyException;
import javafx.util.Pair;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIProc;
import model.type.IType;

import java.util.List;

public class ProcStmt implements IStmt{
    private String name;
    private List<String> variables;
    private IStmt body;

    public ProcStmt(String name, List<String> variables, IStmt body) {
        this.name = name;
        this.variables = variables;
        this.body = body;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIProc procTable = state.getProcTable();
        procTable.put(name, new Pair<>(variables, body));
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return name + "(" + variables + "){" + body.toString() + "}" ;
    }
}
