package exception;

public class DictionaryException extends MyException{
    public DictionaryException(String msg) {
        super("Dictionary: " + msg);
    }
}
