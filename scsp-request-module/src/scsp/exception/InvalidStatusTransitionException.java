package scsp.exception;

public class InvalidStatusTransitionException extends IllegalStateException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
