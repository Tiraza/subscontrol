package br.com.subscontrol.application.provider.content.retrieve.get;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;

import java.util.Objects;

public class DefaultGetContentProviderUseCase extends GetContentProviderUseCase {

    private final ContentProviderGateway gateway;

    public DefaultGetContentProviderUseCase(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public ContentProviderOutput execute(final String id) {
        final ContentProviderID providerID = ContentProviderID.from(id);
        return this.gateway.findById(providerID)
                .map(ContentProviderOutput::from)
                .orElseThrow(() -> NotFoundException.with(ContentProvider.class, providerID));
    }
}
