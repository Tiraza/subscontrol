package br.com.subscontrol.application.provider.sub.update;

import br.com.subscontrol.domain.Identifier;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateSubProviderUseCase extends UpdateSubProviderUseCase {

    private SubProviderGateway gateway;

    public DefaultUpdateSubProviderUseCase(final SubProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateSubProviderOutput execute(final UpdateSubProviderCommand command) {
        SubProviderID providerID = SubProviderID.from(command.id());
        SubProvider provider = this.gateway.findById(providerID).orElseThrow(notFound(providerID));
        provider.update(
                command.name(),
                command.baseUrl(),
                command.active(),
                command.clientId(),
                command.clientSecret(),
                command.authorizationUrl(),
                command.tokenUrl());
        return UpdateSubProviderOutput.from(this.gateway.update(provider));
    }

    private Supplier<DomainException> notFound(final Identifier id) {
        return () -> NotFoundException.with(SubProvider.class, id);
    }
}