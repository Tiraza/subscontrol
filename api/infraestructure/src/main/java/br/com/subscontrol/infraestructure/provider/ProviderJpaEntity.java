package br.com.subscontrol.infraestructure.provider;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.Instant;

@MappedSuperclass
public class ProviderJpaEntity {

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "base_url", nullable = false)
    protected String baseUrl;

    @Column(name = "client_id")
    protected String clientId;

    @Column(name = "client_secret")
    protected String clientSecret;

    @Column(name = "authorization_url")
    protected String authorizationUrl;

    @Column(name = "token_url")
    protected String tokenUrl;

    @Column(name = "active", nullable = false)
    protected boolean active;

    @Column(name = "last_sync", columnDefinition = "TIMESTAMP(6)")
    protected Instant lastSync;

    public Authentication getAuthentication() {
        return new Authentication(
                getClientId(),
                getClientSecret(),
                getAuthorizationUrl(),
                getTokenUrl()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getLastSync() {
        return lastSync;
    }

    public void setLastSync(Instant lastSync) {
        this.lastSync = lastSync;
    }
}
