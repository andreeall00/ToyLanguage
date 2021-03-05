package exception;

public class LatchException extends MyException{
    public LatchException(String msg) {
        super("Latch: " + msg);
    }
}
