package exception;

public class LockException  extends MyException{
    public LockException(String msg) {
        super("Lock: " + msg);
    }
}
