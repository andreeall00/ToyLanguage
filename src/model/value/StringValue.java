package model.value;


import exception.MyException;
import model.type.IType;
import model.type.StringType;

public class StringValue implements IValue {
    private String val;

    public StringValue(String v) {
        val = v;
    }

    public String getVal() {
        return val;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public boolean equals(IValue other) throws MyException {
        if (!other.getType().equals(new StringType()))
            throw new MyException("Different types");
        return val.equals(((StringValue) other).getVal());
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
