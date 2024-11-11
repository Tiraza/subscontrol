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

    protected Tier(
            final TierID id,
            final String providedId,
            final String title,
            final String description,
            final String amount,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id, providedId, active, createdAt, updatedAt, deletedAt);
        this.title = title;
        this.description = description;
        this.amount = amount;
        selfValidate();
    }

    public static Tier newTier(final String providedId, final String title, final String description, final String amount) {
        final Instant now = Instant.now();
        return new Tier(TierID.unique(), providedId, title, description, amount, true, now, now, null);
    }

    public static Tier with(
            final String id,
            final String providedId,
            final String title,
            final String description,
            final String amount,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Tier(TierID.from(id), providedId, title, description, amount, active, createdAt, updatedAt, deletedAt);
    }

    public void update(final String title, final String description, final String amount, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.updatedAt = InstantUtils.now();
        selfValidate();
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
