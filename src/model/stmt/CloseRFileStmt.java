package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.exp.IExp;
import model.type.IType;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStmt implements IStmt {
    private IExp exp;

    public CloseRFileStmt(IExp e) {
        exp = e;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap<IValue> heap = state.getHeap();
        IValue val = exp.eval(symTable, heap);
        if (val.getType().equals(new StringType())) {
            if (fileTable.isDefined((StringValue)val)) {
                BufferedReader bf = fileTable.lookup((StringValue)val);
                try {
                    bf.close();
                }
                catch (IOException e) {
                    throw new MyException(e.getMessage());
                }
                fileTable.remove((StringValue)val);
            } else
                throw new MyException("Close Reading File Statement Execution: file is not in the table");
        } else
            throw new MyException("Close Reading File Statement Execution: expression evaluation is not a string");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((exp.typeCheck(typeEnv)).equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Close Reading File Statement TypeCheck: expression evaluation is not a string");
    }

    @Override
    public String toString() {
        return "Close(" + exp.toString() + ")";
    }
}
