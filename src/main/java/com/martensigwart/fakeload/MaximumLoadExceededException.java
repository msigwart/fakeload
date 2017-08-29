package com.martensigwart.fakeload;

/**
 * Exception indicating the exceeding of load level.
 *
 * For example a CPU load of over 100%.
 */
public class MaximumLoadExceededException extends Exception {
    private static final long serialVersionUID = 8933703494492611173L;

    public MaximumLoadExceededException(String message) {
        super(message);
    }

    public MaximumLoadExceededException(Throwable cause) {
        super(cause);
    }

    public MaximumLoadExceededException(String message, Throwable cause) {
        super(message, cause);
    }

}
