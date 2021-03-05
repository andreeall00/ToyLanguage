package exception;

public class ListException extends MyException{
    public ListException(String msg) {
        super("List: " + msg);
    }
}