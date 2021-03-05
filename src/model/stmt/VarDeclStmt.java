package model.stmt;

import exception.ExecException;
import exception.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.type.*;
import model.value.*;

public class VarDeclStmt implements IStmt {
    private String name;
    private IType typ;

    public VarDeclStmt(String n, IType t) {
        name = n;
        typ = t;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable().top();
        if (symTbl.isDefined(name))
            throw new ExecException("Variable Declaration Statement Execution: variable already exists");
        if (typ.equals(new IntType()))
            symTbl.put(name, new IntValue(0));
        else if (typ.equals(new BoolType()))
            symTbl.put(name, new BoolValue(false));
        else if (typ.equals(new StringType()))
            symTbl.put(name, new StringValue(""));
        else if (typ.equals(new RefType(new IntType())))
            symTbl.put(name, new RefValue(0, new IntType()));
        else if (typ.equals(new RefType(new BoolType())))
            symTbl.put(name, new RefValue(0, new BoolType()));
        else if (typ.equals(new RefType(new RefType(new IntType()))))
            symTbl.put(name, new RefValue(0, new RefType(new IntType())));
        else if (typ.equals(new RefType(new RefType(new RefType(new StringType())))))
            symTbl.put(name, new RefValue(0, new RefType(new RefType(new StringType()))));
        else if (typ.equals(new RefType(new RefType(new RefType(new RefType(new BoolType()))))))
            symTbl.put(name, new RefValue(0, new RefType(new RefType(new RefType(new BoolType())))));
        else throw new ExecException("Variable Declaration Statement Execution: invalid type");
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        typeEnv.put(name, typ);
        return typeEnv;
    }

    @Override
    public String toString() {
        return typ + " " + name;
    }
}
