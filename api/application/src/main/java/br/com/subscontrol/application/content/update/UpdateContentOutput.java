package br.com.subscontrol.application.content.update;

import br.com.subscontrol.domain.content.Content;

public record UpdateContentOutput(String id) {

    public static UpdateContentOutput from(final Content content) {
        return new UpdateContentOutput(content.getId().getValue());
    }

}
