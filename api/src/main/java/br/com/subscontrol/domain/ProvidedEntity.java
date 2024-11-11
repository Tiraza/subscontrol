package br.com.subscontrol.domain;

import br.com.subscontrol.domain.utils.InstantUtils;

import java.time.Instant;

public abstract class ProvidedEntity<ID extends Identifier> extends Entity<ID> {

    protected String providedId;
    protected boolean active;
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;

    public ProvidedEntity(ID id, String providedId, boolean active, Instant createdAt, Instant updatedAt, Instant deletedAt) {
        super(id);
        this.providedId = providedId;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = active ? null : deletedAt;
    }

    public String getProvidedId() {
        return providedId;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
    }

    public void activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
    }
}
