package se.zipper.hwcertifier.exception;

public class BadParametersException extends IllegalArgumentException {
    public BadParametersException() {
    }

    public BadParametersException(String s) {
        super(s);
    }

    public BadParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadParametersException(Throwable cause) {
        super(cause);
    }
}
