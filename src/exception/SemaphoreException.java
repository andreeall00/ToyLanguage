package exception;

public class SemaphoreException extends MyException {
    public SemaphoreException(String msg) {
        super("Semaphore: " + msg);
    }
}
