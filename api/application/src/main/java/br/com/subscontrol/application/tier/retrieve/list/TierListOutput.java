package br.com.subscontrol.application.tier.retrieve.list;

import br.com.subscontrol.domain.tier.Tier;

public record TierListOutput(
        String id,
        String title,
        String amount,
        boolean active
) {

    public static TierListOutput from(final Tier tier) {
        return new TierListOutput(
                tier.getId().getValue(),
                tier.getTitle(),
                tier.getAmount(),
                tier.isActive());
    }
}
