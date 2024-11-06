package br.com.subscontrol.domain.validation.handler;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.validation.ErrorMessage;
import br.com.subscontrol.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<ErrorMessage> errors;

    private Notification(final List<ErrorMessage> errorsList) {
        this.errors = errorsList;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t) {
        return new Notification(new ArrayList<>()).append(new ErrorMessage(t.getMessage()));
    }

    public static Notification create(final ErrorMessage error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(final ErrorMessage anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new ErrorMessage(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return this.errors;
    }
}

