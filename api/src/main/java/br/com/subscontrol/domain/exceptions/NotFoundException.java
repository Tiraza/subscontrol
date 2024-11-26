package br.com.subscontrol.domain.exceptions;

import br.com.subscontrol.domain.Entity;
import br.com.subscontrol.domain.Identifier;
import br.com.subscontrol.domain.validation.ErrorMessage;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String aMessage, final List<ErrorMessage> errors) {
        super(aMessage, errors);
    }

    public static NotFoundException with(final Class<? extends Entity<?>> anAggregate, final Identifier id) {
        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(),
                id.getValue()
        );
        return new NotFoundException(anError, Collections.emptyList());
    }

    public static NotFoundException with(final Class<?> clazz, final String value) {
        final var anError = "%s with value %s was not found".formatted(
                clazz.getSimpleName(),
                value
        );
        return new NotFoundException(anError, Collections.emptyList());
    }
}
