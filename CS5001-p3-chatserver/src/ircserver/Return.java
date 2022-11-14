package ircserver;

/**
 * Enum to hold server return codes.
 */
public enum Return {
    /**
     * Return code for NICK.
     */
    NICK(1),
    /**
     * Return code for LIST.
     */
    LIST(322),
    /**
     * Return code for LIST (FINAL).
     */
    LIST_END(323),
    /**
     * Return code for NAMES.
     */
    CHANNELS(353),
    /**
     * Return code for INFO.
     */
    INFO(371),
    /**
     * Return code for TIME.
     */
    TIME(391),
    /**
     * Return code for any error.
     */
    ERROR(400);

    private final int code;

    Return(int code) {
        this.code = code;
    }

    /**
     * Method to get return code from Enum.
     * @return  int
     */
    public int getCode() {
        return code;
    }
}
