package br.com.subscontrol.application.content.update;

import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.exceptions.NotFoundException;

public class DefaultUpdateContentUseCase extends UpdateContentUseCase {

    private final ContentGateway gateway;

    public DefaultUpdateContentUseCase(ContentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public UpdateContentOutput execute(final UpdateContentCommand command) {
        ContentID contentID = ContentID.from(command.id());
        Content content = this.gateway.findById(contentID)
                .orElseThrow(NotFoundException.notFound(Content.class, contentID));
        content.update(
                command.label(),
                command.providedId(),
                command.active()
        );
        return UpdateContentOutput.from(this.gateway.update(content));
    }

}
