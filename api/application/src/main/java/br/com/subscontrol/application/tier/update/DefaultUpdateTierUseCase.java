package br.com.subscontrol.application.tier.update;

import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.sub.SubID;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierGateway;
import br.com.subscontrol.domain.tier.TierID;

import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultUpdateTierUseCase extends UpdateTierUseCase {

    private final TierGateway gateway;

    public DefaultUpdateTierUseCase(TierGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateTierOutput execute(final UpdateTierCommand command) {
        TierID tierID = TierID.from(command.id());
        Tier tier = this.gateway.findById(tierID)
                .orElseThrow(NotFoundException.notFound(Tier.class, tierID));
        tier.update(
                command.title(),
                command.description(),
                command.amount(),
                command.subTiers().stream().map(TierID::from).collect(Collectors.toList()),
                command.subscribers().stream().map(SubID::from).collect(Collectors.toList()),
                command.contents().stream().map(ContentID::from).collect(Collectors.toList()),
                command.active()
        );
        return UpdateTierOutput.from(this.gateway.update(tier));
    }
}
