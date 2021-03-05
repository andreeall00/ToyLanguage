package exception;

public class StackException extends MyException{
    public StackException(String msg) {
        super("Stack: " + msg);
    }
}
