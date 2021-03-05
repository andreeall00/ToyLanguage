package exception;

public class BarrierException  extends MyException{
    public BarrierException(String msg) {
        super("Barrier: " + msg);
    }
}
