package model.type;

import model.value.IValue;
import model.value.StringValue;

public class StringType implements IType {
    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }
}
