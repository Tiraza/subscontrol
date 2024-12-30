package br.com.subscontrol.application.provider.content.create;

import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;

import java.util.Objects;

public class DefaultCreateContentProviderUseCase extends CreateContentProviderUseCase {

    private final ContentProviderGateway gateway;

    public DefaultCreateContentProviderUseCase(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateContentProviderOutput execute(final CreateContentProviderCommand command) {
        ContentProvider provider = ContentProvider.create(
                command.type(),
                command.name(),
                command.baseUrl(),
                command.clientId(),
                command.clientSecret(),
                command.authorizationUrl(),
                command.tokenUrl());

        return CreateContentProviderOutput.from(this.gateway.create(provider));
    }
}
