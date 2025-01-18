package br.com.subscontrol.application.provider.content.update;

import br.com.subscontrol.domain.provider.content.ContentProvider;

public record UpdateContentProviderOutput(String id) {

    public static UpdateContentProviderOutput from(final String id) {
        return new UpdateContentProviderOutput(id);
    }

    public static UpdateContentProviderOutput from(final ContentProvider provider) {
        return new UpdateContentProviderOutput(provider.getId().getValue());
    }

}
