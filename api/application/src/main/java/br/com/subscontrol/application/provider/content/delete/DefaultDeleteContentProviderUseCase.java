package br.com.subscontrol.application.provider.content.delete;

import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;

import java.util.Objects;

public class DefaultDeleteContentProviderUseCase extends DeleteContentProviderUseCase {

    private final ContentProviderGateway gateway;

    public DefaultDeleteContentProviderUseCase(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(ContentProviderID.from(id));
    }
}
