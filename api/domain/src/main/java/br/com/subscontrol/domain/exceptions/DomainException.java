package br.com.subscontrol.domain.exceptions;

import br.com.subscontrol.domain.validation.ErrorMessage;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<ErrorMessage> errors;

    protected DomainException(final String message, final List<ErrorMessage> anErrors) {
        super(message);
        this.errors = anErrors;
    }

    public static DomainException with(final String message) {
        return new DomainException(message, List.of(new ErrorMessage(message)));
    }

    public static DomainException with(final String message, final List<ErrorMessage> errors) {
        return new DomainException(message, errors);
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public ErrorMessage firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().getFirst();
        }
        return null;
    }
}
