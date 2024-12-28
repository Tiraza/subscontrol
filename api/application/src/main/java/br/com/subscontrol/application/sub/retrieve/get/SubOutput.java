package br.com.subscontrol.application.sub.retrieve.get;

import br.com.subscontrol.domain.sub.Sub;

import java.time.Instant;

public record SubOutput(
        String id,
        String name,
        String email,
        String providedId,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static SubOutput from(final Sub sub) {
        return new SubOutput(
                sub.getId().getValue(),
                sub.getName(),
                sub.getEmail(),
                sub.getProvidedId(),
                sub.isActive(),
                sub.getCreatedAt(),
                sub.getUpdatedAt(),
                sub.getDeletedAt()
        );
    }
}
