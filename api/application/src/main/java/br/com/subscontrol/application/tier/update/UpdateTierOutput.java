package br.com.subscontrol.application.tier.update;

import br.com.subscontrol.domain.tier.Tier;

public record UpdateTierOutput(String id) {

    public static UpdateTierOutput from(final Tier tier) {
        return new UpdateTierOutput(tier.getId().getValue());
    }
}
