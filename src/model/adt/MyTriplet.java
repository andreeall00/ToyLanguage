package model.adt;


public class MyTriplet<T1,T2,T3> implements MyITriplet<T1,T2,T3> {
    private T1 value1;
    private T2 value2;
    private T3 value3;

    public MyTriplet(T1 value1, T2 value2, T3 value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    @Override
    public T1 getValue1() {
        return value1;
    }

    @Override
    public T2 getValue2() {
        return value2;
    }

    @Override
    public T3 getValue3() {
        return value3;
    }

    @Override
    public String toString() {
        return "triplet(" + value1 + "," + value2 + "," + value3 + ")";
    }
}
