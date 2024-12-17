package br.com.subscontrol.application.content.retrieve.list;

import br.com.subscontrol.domain.content.Content;

public record ContentListOutput(
        String id,
        String label,
        boolean active
) {

    public static ContentListOutput from(final Content content) {
        return new ContentListOutput(
                content.getId().getValue(),
                content.getLabel(),
                content.isActive()
        );
    }
}
