package br.com.subscontrol.domain.exceptions;

public class NoStackTraceException extends RuntimeException {

    public NoStackTraceException(final String message) {
        super(message, null, true, false);
    }

}
