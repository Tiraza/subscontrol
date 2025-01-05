package br.com.subscontrol.domain.validation.handler;

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

    public static Notification create(final ErrorMessage anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(final ErrorMessage anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return this.errors;
    }
}

