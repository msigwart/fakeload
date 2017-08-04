package com.martensigwart.fakeload;

/**
 * Created by martensigwart on 25.07.17.
 */
class MaximumLoadExceededException extends Exception {
    private static final long serialVersionUID = 8933703494492611173L;

    private static final String MSG_FORMAT = "Increase of %d would exceed the maximum %s load limit of %d %s";


    MaximumLoadExceededException(long increase, long maxValue, SimulationType simulationType) {
        this(String.format(MSG_FORMAT, increase, simulationType, maxValue, simulationType.getUnit()));
    }

    MaximumLoadExceededException(long increase, long maxValue, SimulationType simulationType, Throwable cause) {
        this(String.format(MSG_FORMAT, increase, simulationType, maxValue, simulationType.getUnit()), cause);
    }

    MaximumLoadExceededException(Throwable cause) {
        super(cause);
    }

    private MaximumLoadExceededException(String message) {
        super(message);
    }

    private MaximumLoadExceededException(String message, Throwable cause) {
        super(message, cause);
    }


}
