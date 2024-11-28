package br.com.subscontrol.application.provider.content.update;

import br.com.subscontrol.domain.Identifier;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpadteContentProviderUseCase extends UpdateContentProviderUseCase {

    private final ContentProviderGateway gateway;

    public DefaultUpadteContentProviderUseCase(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateContentProviderOutput execute(final UpdateContentProviderCommand command) {
        ContentProviderID providerID = ContentProviderID.from(command.id());
        ContentProvider provider = this.gateway.findById(providerID).orElseThrow(notFound(providerID));
        provider.update(
                command.name(),
                command.baseUrl(),
                command.active(),
                command.clientId(),
                command.clientSecret(),
                command.authorizationUrl(),
                command.tokenUrl());
        return UpdateContentProviderOutput.from(this.gateway.update(provider));
    }

    private Supplier<DomainException> notFound(final Identifier id) {
        return () -> NotFoundException.with(ContentProvider.class, id);
    }
}
