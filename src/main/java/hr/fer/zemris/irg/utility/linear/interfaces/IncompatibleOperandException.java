package hr.fer.zemris.irg.utility.linear.interfaces;

/**
 * @author Filip Gulan
 */
public class IncompatibleOperandException extends RuntimeException {

    public IncompatibleOperandException() {
        super();
    }

    public IncompatibleOperandException(String message) {
        super(message);
    }

    public IncompatibleOperandException(Throwable cause) {
        super(cause);
    }

    public IncompatibleOperandException(String message, Throwable cause) {
        super(message, cause);
    }
}