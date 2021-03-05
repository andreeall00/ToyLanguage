package exception;

public class ProcException extends MyException{
    public ProcException(String msg) {
        super("Procedure: " + msg);
    }
}
