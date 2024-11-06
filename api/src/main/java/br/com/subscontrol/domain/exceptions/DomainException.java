package br.com.subscontrol.domain.exceptions;

import br.com.subscontrol.domain.validation.ErrorMessage;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<ErrorMessage> errors;

    protected DomainException(final String message, final List<ErrorMessage> anErrors) {
        super(message);
        this.errors = anErrors;
    }

    public static DomainException with(final ErrorMessage anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public static DomainException with(final List<ErrorMessage> errors) {
        return new DomainException("", errors);
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }
}
