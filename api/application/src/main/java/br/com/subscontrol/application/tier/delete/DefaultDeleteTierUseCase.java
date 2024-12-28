package br.com.subscontrol.application.tier.delete;

import br.com.subscontrol.domain.tier.TierGateway;
import br.com.subscontrol.domain.tier.TierID;

import java.util.Objects;

public class DefaultDeleteTierUseCase extends DeleteTierUseCase {

    private final TierGateway gateway;

    public DefaultDeleteTierUseCase(TierGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(TierID.from(id));
    }

}
