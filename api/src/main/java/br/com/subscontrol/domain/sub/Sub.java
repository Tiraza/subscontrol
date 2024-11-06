package br.com.subscontrol.domain.sub;

import br.com.subscontrol.domain.Entidade;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.handler.Notification;

import java.time.Instant;

public class Sub extends Entidade<SubID> {

    private String name;
    private String email;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Sub(
            final SubID id,
            final String name,
            final String email,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id);
        this.name = name;
        this.email = email;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        selfValidate();
    }

    public static Sub newSub(final String nome, final String email) {
        final var now = Instant.now();
        return new Sub(SubID.unique(), nome, email, true, now, now, null);
    }

    public static Sub with(
            final String id,
            final String name,
            final String email,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Sub(SubID.from(id), name, email, isActive, createdAt, updatedAt, deletedAt);
    }

    public Sub update(final String name, final String email, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.email = email;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    public Sub deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Sub activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new SubValidator(this, handler).validate();
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            //throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }
}
