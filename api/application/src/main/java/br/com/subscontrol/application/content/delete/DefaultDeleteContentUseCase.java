package br.com.subscontrol.application.content.delete;

import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.content.ContentID;

import java.util.Objects;

public class DefaultDeleteContentUseCase extends DeleteContentUseCase {

    private final ContentGateway gateway;

    public DefaultDeleteContentUseCase(ContentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(ContentID.from(id));
    }
}
