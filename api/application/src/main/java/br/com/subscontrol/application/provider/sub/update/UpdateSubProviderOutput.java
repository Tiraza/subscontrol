package br.com.subscontrol.application.provider.sub.update;

import br.com.subscontrol.domain.provider.sub.SubProvider;

public record UpdateSubProviderOutput(String id) {

    public static UpdateSubProviderOutput from(final String id) {
        return new UpdateSubProviderOutput(id);
    }

    public static UpdateSubProviderOutput from(final SubProvider provider) {
        return new UpdateSubProviderOutput(provider.getId().getValue());
    }

}