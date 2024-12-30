package br.com.subscontrol.application.provider.sub.create;

import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;

import java.util.Objects;

public class DefaultCreateSubProviderUseCase extends CreateSubProviderUseCase {

    private SubProviderGateway gateway;

    public DefaultCreateSubProviderUseCase(final SubProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateSubProviderOutput execute(final CreateSubProviderCommand command) {
        SubProvider provider = SubProvider.create(
                command.type(),
                command.name(),
                command.baseUrl(),
                command.clientId(),
                command.clientSecret(),
                command.authorizationUrl(),
                command.tokenUrl());

        return CreateSubProviderOutput.from(this.gateway.create(provider));
    }
}
