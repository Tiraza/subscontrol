package br.com.subscontrol.application.content.retrieve.get;

import br.com.subscontrol.domain.content.Content;

import java.time.Instant;

public record ContentOutput(
        String id,
        String providerID,
        String providedId,
        String label,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static ContentOutput from(final Content content) {
        return new ContentOutput(
                content.getId().getValue(),
                content.getContentProviderID().getValue(),
                content.getProvidedId(),
                content.getLabel(),
                content.isActive(),
                content.getCreatedAt(),
                content.getUpdatedAt(),
                content.getDeletedAt()
        );
    }
}
