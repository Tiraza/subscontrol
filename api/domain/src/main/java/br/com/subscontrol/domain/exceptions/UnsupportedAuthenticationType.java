package br.com.subscontrol.domain.exceptions;

import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.validation.ErrorMessage;

import java.util.Collections;
import java.util.List;

public class UnsupportedAuthenticationType extends DomainException {

    protected UnsupportedAuthenticationType(final String message, final List<ErrorMessage> anErrors) {
        super(message, anErrors);
    }

    public static UnsupportedAuthenticationType with(final AuthenticationType type) {
        final var anError = "Unsupported authentication type '%s'".formatted(type.name());
        return new UnsupportedAuthenticationType(anError, Collections.emptyList());
    }

}
