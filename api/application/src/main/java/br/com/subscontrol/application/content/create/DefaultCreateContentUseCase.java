package br.com.subscontrol.application.content.create;

import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;

public class DefaultCreateContentUseCase extends CreateContentUseCase {

    private final ContentGateway gateway;

    public DefaultCreateContentUseCase(ContentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public CreateContentOutput execute(final CreateContentCommand command) {
        Content content = Content.create(
                command.providedId(),
                command.label());

        return CreateContentOutput.from(this.gateway.create(content));
    }
}
