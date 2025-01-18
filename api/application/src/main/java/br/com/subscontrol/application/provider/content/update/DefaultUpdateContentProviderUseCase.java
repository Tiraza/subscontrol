package br.com.subscontrol.application.provider.content.update;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;

import java.util.Objects;

public class DefaultUpdateContentProviderUseCase extends UpdateContentProviderUseCase {

    private final ContentProviderGateway gateway;

    public DefaultUpdateContentProviderUseCase(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateContentProviderOutput execute(final UpdateContentProviderCommand command) {
        ContentProviderID providerID = ContentProviderID.from(command.id());
        ContentProvider provider = this.gateway.findById(providerID)
                .orElseThrow(NotFoundException.notFound(ContentProvider.class, providerID));
        provider.update(command.name(), command.baseUrl(), command.active());
        provider.updateAuthentication(
                command.authenticationType(),
                command.clientId(),
                command.clientSecret(),
                command.authorizationUrl(),
                command.tokenUrl(),
                command.fileBase64()
        );
        return UpdateContentProviderOutput.from(this.gateway.update(provider));
    }

}
