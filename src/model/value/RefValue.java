package model.value;

import exception.MyException;
import model.type.IType;
import model.type.RefType;

public class RefValue implements IValue{
    private int address;
    private IType locationType;

    public RefValue(int a, IType lt) {
        address = a;
        locationType = lt;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setLocationType(IType locationType) {
        this.locationType = locationType;
    }

    public IType getLocationType() {
        return locationType;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    @Override
    public boolean equals(IValue other) throws MyException {
        if(!other.getType().equals(new RefType(locationType)))
            throw new MyException("Different types");
        return address==((RefValue)other).getAddress();
    }

    @Override
    public String toString() {
        return "(" + address + "," + locationType + ")";
    }
}
