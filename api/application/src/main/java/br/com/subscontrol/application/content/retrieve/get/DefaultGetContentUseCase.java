package br.com.subscontrol.application.content.retrieve.get;

import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.exceptions.NotFoundException;

public class DefaultGetContentUseCase extends GetContentUseCase {

    private final ContentGateway gateway;

    public DefaultGetContentUseCase(ContentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public ContentOutput execute(final String id) {
        final ContentID contentID = ContentID.from(id);
        return this.gateway.findById(contentID)
                .map(ContentOutput::from)
                .orElseThrow(NotFoundException.notFound(Content.class, contentID));
    }
}
