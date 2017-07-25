package ac.at.tuwien.infosys.fakeload.internal;

/**
 * Created by martensigwart on 25.07.17.
 */
class MaximumLoadExceededException extends Exception {
    private static final long serialVersionUID = 8933703494492611173L;


    MaximumLoadExceededException(String message) {
        super(message);
    }

    MaximumLoadExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    MaximumLoadExceededException(Throwable cause) {
        super(cause);
    }

}
