package br.com.subscontrol.domain;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.handler.Notification;

public abstract class ValueObject {

    protected abstract void validate(ValidationHandler handler);

    protected void selfValidate() {
        final var notification = Notification.create();

        validate(notification);

        if (notification.hasError()) {
            throw DomainException.with("Failed to create ValueObject", notification.getErrors());
        }
    }
}
