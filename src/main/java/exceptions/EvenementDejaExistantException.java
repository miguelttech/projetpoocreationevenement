package exceptions;

public class EvenementDejaExistantException extends Exception {
    public EvenementDejaExistantException(String message) {
        super(message);
    }
}