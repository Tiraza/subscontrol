package br.com.subscontrol.domain.provider.authentication;

import br.com.subscontrol.domain.exceptions.UnsupportedAuthenticationType;
import br.com.subscontrol.domain.validation.ErrorMessage;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.Validator;

import static br.com.subscontrol.domain.utils.ValidationUtils.checkIdentifierConstraints;
import static br.com.subscontrol.domain.utils.ValidationUtils.checkStringConstraints;

public class AuthenticationValidator extends Validator {

    private final Authentication authentication;

    protected AuthenticationValidator(final Authentication authentication, final ValidationHandler handler) {
        super(handler);
        this.authentication = authentication;
    }

    @Override
    public void validate() {
        checkIdentifierConstraints(authentication.getProviderID(), "ProviderID", validationHandler());
        AuthenticationType type = authentication.getType();
        switch (type) {
            case AuthenticationType.CLIENT_SECRET -> checkClientSecretConstraints();
            case AuthenticationType.FILE -> checkFileConstraints();
            default -> throw UnsupportedAuthenticationType.with(type);
        }
    }

    private void checkClientSecretConstraints() {
        checkStringConstraints(authentication.getClientId(), "clientId", validationHandler());
        checkStringConstraints(authentication.getClientSecret(), "clientSecret", validationHandler());
        checkStringConstraints(authentication.getAuthorizationUrl(), "authorizationUrl", validationHandler());
        checkStringConstraints(authentication.getTokenUrl(), "tokenUrl", validationHandler());
    }

    private void checkFileConstraints() {
        if (authentication.getFile() == null) {
            validationHandler().append(new ErrorMessage("'file' should not be null"));
        }
    }
}
