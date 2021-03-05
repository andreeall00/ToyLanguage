package model.stmt;

import exception.MyException;
import model.PrgState;
import model.adt.IHeap;
import model.adt.MyIDictionary;
import model.exp.IExp;
import model.type.IType;
import model.type.IntType;
import model.type.StringType;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt {
    private IExp exp;
    private String varName;

    public ReadFileStmt(IExp e, String v) {
        exp = e;
        varName = v;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().top();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap<IValue> heap = state.getHeap();
        if (symTable.isDefined(varName)){
            if(symTable.lookup(varName).getType().equals(new IntType())){
                IValue val = exp.eval(symTable, heap);
                if (val.getType().equals(new StringType())){
                    if (fileTable.isDefined((StringValue)val)) {
                        BufferedReader bf = fileTable.lookup((StringValue)val);
                        try {
                            String line = bf.readLine();
                            IValue v;
                            if(line == null)
                                v = new IntValue(0);
                            else
                                v = new IntValue(Integer.parseInt(line));
                            symTable.update(varName, v);
                        }
                        catch (IOException e) {
                            throw new MyException(e.getMessage());
                        }
                    }
                    else
                        throw new MyException("Read File Statement Execution: file is not in the table");
                }
                else
                    throw new MyException("Read File Statement Execution: expression evaluation is not a string");
            }
            else
                throw new MyException("Read File Statement Execution: variable" + varName + "is not an integer");
        }
        else
            throw new MyException("Read File Statement Execution: variable" + varName + "is not defined");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if ((exp.typeCheck(typeEnv)).equals(new StringType()))
            if (typeEnv.lookup(varName).equals(new IntType()))
                return typeEnv;
            else
                throw new MyException("Read File Statement TypeCheck:  variable" + varName + "is not an integer");
        else
            throw new MyException("Read File Statement TypeCheck: expression evaluation is not a string");
    }

    @Override
    public String toString() {
        return "Read(" + exp.toString() + " into " + varName + ")";
    }
}
