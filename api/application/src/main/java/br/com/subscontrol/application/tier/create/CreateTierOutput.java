package br.com.subscontrol.application.tier.create;

import br.com.subscontrol.domain.tier.Tier;

public record CreateTierOutput(String id) {

    public static CreateTierOutput from(final Tier tier) {
        return new CreateTierOutput(tier.getId().getValue());
    }
}
