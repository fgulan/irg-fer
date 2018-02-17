package hr.fer.zemris.irg.utility.linear.interfaces;

/**
 * @author Filip Gulan
 */
public class UnmodifiableObjectException extends RuntimeException {

    public UnmodifiableObjectException() {
        super();
    }

    public UnmodifiableObjectException(String message) {
        super(message);
    }

    public UnmodifiableObjectException(Throwable cause) {
        super(cause);
    }

    public UnmodifiableObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
