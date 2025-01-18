package br.com.subscontrol.application.provider.content.create;

import br.com.subscontrol.domain.provider.content.ContentProvider;

public record CreateContentProviderOutput(String id) {

    public static CreateContentProviderOutput from(final String anId) {
        return new CreateContentProviderOutput(anId);
    }

    public static CreateContentProviderOutput from(final ContentProvider provider) {
        return new CreateContentProviderOutput(provider.getId().getValue());
    }

}
