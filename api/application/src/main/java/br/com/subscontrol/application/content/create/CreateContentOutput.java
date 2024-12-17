package br.com.subscontrol.application.content.create;

import br.com.subscontrol.domain.content.Content;

public record CreateContentOutput(String id) {

    public static CreateContentOutput from(final Content content) {
        return new CreateContentOutput(content.getId().getValue());
    }

}
