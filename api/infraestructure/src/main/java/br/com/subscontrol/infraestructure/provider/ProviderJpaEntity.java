package br.com.subscontrol.infraestructure.provider;

import br.com.subscontrol.infraestructure.provider.authentication.AuthenticationJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@MappedSuperclass
public abstract class ProviderJpaEntity {

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "base_url")
    protected String baseUrl;

    @Column(name = "active", nullable = false)
    protected boolean active;

    @Column(name = "last_sync", columnDefinition = "TIMESTAMP(6)")
    protected Instant lastSync;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "id", referencedColumnName = "provider_id")
    protected AuthenticationJpaEntity authentication;

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

    public AuthenticationJpaEntity getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationJpaEntity authentication) {
        this.authentication = authentication;
    }
}
