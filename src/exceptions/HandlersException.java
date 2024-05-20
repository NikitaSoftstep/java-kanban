package exceptions;

public class HandlersException extends RuntimeException {
    public HandlersException(String message, Exception e) {
        super("Возникло непредвиденное исключение" + message);
        e.getStackTrace();
    }
}
