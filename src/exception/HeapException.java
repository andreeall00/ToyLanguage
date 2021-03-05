package exception;

public class HeapException extends MyException{
    public HeapException(String msg) {
        super("Heap: " + msg);
    }
}
