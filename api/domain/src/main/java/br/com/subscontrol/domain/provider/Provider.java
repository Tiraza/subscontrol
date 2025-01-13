package br.com.subscontrol.domain.provider;

import br.com.subscontrol.domain.Entity;
import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Optional;

public abstract class Provider<ID extends ProviderID> extends Entity<ID> {

    protected String name;
    protected String baseUrl;
    protected boolean active;
    protected Instant lastSync;
    protected Authentication authentication;

    public Provider(ID id, String name, String baseUrl, boolean active, Instant lastSync, Authentication authentication) {
        super(id);
        this.name = name;
        this.baseUrl = baseUrl;
        this.active = active;
        this.lastSync = lastSync;
        this.authentication = authentication;
    }

    public void updateAuthentication(
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        Optional.ofNullable(authentication).ifPresentOrElse(
                auth -> auth.updateClientSecret(clientId, clientSecret, authorizationUrl, tokenUrl),
                () -> authentication = Authentication.withClientSecret(this.id, clientId, clientSecret, authorizationUrl, tokenUrl)
        );
    }

    public void updateAuthentication(final byte[] file) {
        Optional.ofNullable(authentication).ifPresentOrElse(
                auth -> auth.updateFile(file),
                () -> authentication = Authentication.withFile(this.id, file)
        );
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getLastSync() {
        return lastSync;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new ProviderValidator(this, handler).validate();
    }
}

