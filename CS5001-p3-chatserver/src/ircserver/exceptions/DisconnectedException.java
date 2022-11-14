package ircserver.exceptions;

/**
 * Class for the DisconnectedException.
 */
public class DisconnectedException extends Exception {
    /**
     * Custom Disconnected client exception.
     * @param message
     */
    public DisconnectedException(String message) {
        super(message);
    }
}
