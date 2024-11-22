package br.com.subscontrol.domain.content;

import br.com.subscontrol.domain.ProvidedEntity;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;

import java.time.Instant;

public class Content extends ProvidedEntity<ContentID> {

    private String label;

    protected Content(
            final ContentID id,
            final String providedId,
            final String label,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id, providedId, active, createdAt, updatedAt, deletedAt);
        this.label = label;
        selfValidate();
    }

    public static Content create(final String providedId, final String label) {
        final Instant now = Instant.now();
        return new Content(ContentID.unique(), providedId, label, true, now, now, null);
    }

    public static Content with(
            final String id,
            final String providedId,
            final String label,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Content(ContentID.from(id), providedId, label, isActive, createdAt, updatedAt, deletedAt);
    }

    public void update(final String label, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.label = label;
        this.updatedAt = InstantUtils.now();
        selfValidate();
    }

    public String getLabel() {
        return label;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new ContentValidator(this, handler);
    }
}
