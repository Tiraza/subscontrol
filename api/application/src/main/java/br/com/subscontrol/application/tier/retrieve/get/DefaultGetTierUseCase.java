package br.com.subscontrol.application.tier.retrieve.get;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierGateway;
import br.com.subscontrol.domain.tier.TierID;

import java.util.Objects;

public class DefaultGetTierUseCase extends GetTierUseCase {

    private final TierGateway gateway;

    public DefaultGetTierUseCase(TierGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public TierOutput execute(final String id) {
        final TierID tierID = TierID.from(id);
        return this.gateway.findById(tierID)
                .map(TierOutput::from)
                .orElseThrow(NotFoundException.notFound(Tier.class, tierID));
    }
}
