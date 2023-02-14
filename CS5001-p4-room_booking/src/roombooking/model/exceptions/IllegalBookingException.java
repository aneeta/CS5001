package model.exceptions;

/**
 * Class for the IllegalBookingException.
 */
public class IllegalBookingException extends Exception {
    /**
     * Custom Illegal Booking exception.
     * 
     * @param msg error message
     */
    public IllegalBookingException(String msg) {
        super(msg);
    }

}
