package br.com.subscontrol.domain.provider.authentication;

import br.com.subscontrol.domain.ValueObject;
import br.com.subscontrol.domain.provider.ProviderID;
import br.com.subscontrol.domain.validation.ValidationHandler;

public class Authentication extends ValueObject {

    private final ProviderID providerID;
    private AuthenticationType type;
    private String clientId;
    private String clientSecret;
    private String authorizationUrl;
    private String tokenUrl;
    private byte[] file;

    public Authentication(
            final ProviderID providerID,
            final AuthenticationType type,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl,
            final byte[] file) {
        this.providerID = providerID;
        this.type = type;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
        this.file = file;
        selfValidate();
    }

    public static Authentication withClientSecret(
            final ProviderID providerID,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        return new Authentication(
                providerID,
                AuthenticationType.CLIENT_SECRET,
                clientId,
                clientSecret,
                authorizationUrl,
                tokenUrl,
                null);
    }

    public static Authentication withFile(
            final ProviderID providerID,
            final byte[] file) {
        return new Authentication(
                providerID,
                AuthenticationType.FILE,
                null,
                null,
                null,
                null,
                file);
    }

    public void updateClientSecret(
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        this.type = AuthenticationType.CLIENT_SECRET;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
        this.file = null;
        selfValidate();
    }

    public void updateFile(final byte[] file) {
        this.type = AuthenticationType.FILE;
        this.clientId = null;
        this.clientSecret = null;
        this.authorizationUrl = null;
        this.tokenUrl = null;
        this.file = file;
        selfValidate();
    }

    public ProviderID getProviderID() {
        return providerID;
    }

    public AuthenticationType getType() {
        return type;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public byte[] getFile() {
        return file;
    }

    @Override
    protected void validate(ValidationHandler handler) {
        new AuthenticationValidator(this, handler).validate();
    }
}
