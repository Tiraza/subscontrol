package br.com.subscontrol.application.tier.create;

import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierGateway;

import java.util.Objects;

public class DefaultCreateTierUseCase extends CreateTierUseCase {

    private final TierGateway gateway;

    public DefaultCreateTierUseCase(TierGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateTierOutput execute(final CreateTierCommand command) {
        Tier tier = Tier.create(
                command.subProviderID(),
                command.providedId(),
                command.title(),
                command.description(),
                command.amount()
        );
        return CreateTierOutput.from(this.gateway.create(tier));
    }
}
