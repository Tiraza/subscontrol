package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.ProvidedEntity;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.handler.Notification;

import java.time.Instant;

public class Tier extends ProvidedEntity<TierID> {

    private String title;
    private String description;
    private String amount;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Tier(
            final TierID id,
            final String providedId,
            final String title,
            final String description,
            final String amount,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id, providedId);
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        selfValidate();
    }

    public static Tier newTier(final String providedId, final String title, final String description, final String amount) {
        final var now = Instant.now();
        return new Tier(TierID.unique(), providedId, title, description, amount, now, now, null);
    }

    public static Tier with(
            final String id,
            final String providedId,
            final String title,
            final String description,
            final String amount,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Tier(TierID.from(id), providedId, title, description, amount, createdAt, updatedAt, deletedAt);
    }

    public Tier update(final String title, final String description, final String amount) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
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

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw DomainException.with("Failed to create Entity Tier", notification.getErrors());
        }
    }

    @Override
    public void validate(ValidationHandler handler) {
        new TierValidator(this, handler).validate();
    }

}
