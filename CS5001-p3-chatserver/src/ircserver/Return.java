package ircserver;

public enum Return {
    LIST(322),
    LIST_END(322),
    INFO(371),
    TIME(391);

    private final int code;

    Return(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
