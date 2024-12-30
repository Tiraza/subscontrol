package br.com.subscontrol.application.content.create;

import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;

import java.util.Objects;

public class DefaultCreateContentUseCase extends CreateContentUseCase {

    private final ContentGateway gateway;

    public DefaultCreateContentUseCase(ContentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateContentOutput execute(final CreateContentCommand command) {
        Content content = Content.create(
                command.providerID(),
                command.providedId(),
                command.label());
        return CreateContentOutput.from(this.gateway.create(content));
    }
}
