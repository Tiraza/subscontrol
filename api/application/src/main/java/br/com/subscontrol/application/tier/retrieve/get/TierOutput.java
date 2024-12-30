package br.com.subscontrol.application.tier.retrieve.get;

import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.sub.SubID;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierID;

import java.time.Instant;
import java.util.List;

public record TierOutput(
        String id,
        String subProviderID,
        String providedId,
        String title,
        String description,
        String amount,
        List<String> subTiers,
        List<String> subscribers,
        List<String> contents,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static TierOutput from(final Tier tier) {
        return new TierOutput(
                tier.getId().getValue(),
                tier.getSubProviderID().getValue(),
                tier.getProvidedId(),
                tier.getTitle(),
                tier.getDescription(),
                tier.getAmount(),
                tier.getSubTiers().stream().map(TierID::getValue).toList(),
                tier.getSubscribers().stream().map(SubID::getValue).toList(),
                tier.getContents().stream().map(ContentID::getValue).toList(),
                tier.isActive(),
                tier.getCreatedAt(),
                tier.getUpdatedAt(),
                tier.getDeletedAt()
        );
    }
}
