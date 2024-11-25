package br.com.subscontrol.domain.sub;

import br.com.subscontrol.domain.ProvidedEntity;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;

import java.time.Instant;

public class Sub extends ProvidedEntity<SubID> {

    private String name;
    private String email;

    protected Sub(
            final SubID id,
            final String providedId,
            final String name,
            final String email,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id, providedId, isActive, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.email = email;
        selfValidate();
    }

    public static Sub create(final String providedId, final String name, final String email) {
        final Instant now = Instant.now();
        return new Sub(SubID.unique(), providedId, name, email, true, now, now, null);
    }

    public static Sub with(
            final String id,
            final String providedId,
            final String name,
            final String email,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Sub(SubID.from(id), providedId, name, email, isActive, createdAt, updatedAt, deletedAt);
    }

    public void update(final String name, final String email, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.email = email;
        this.updatedAt = InstantUtils.now();
        selfValidate();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new SubValidator(this, handler).validate();
    }

}
