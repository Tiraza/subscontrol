package br.com.subscontrol.domain.validation;

import java.util.Objects;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler handler) {
        this.handler = Objects.requireNonNull(handler, "'Handler' should not be null");
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return this.handler;
    }
}