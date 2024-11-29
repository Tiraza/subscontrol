package br.com.subscontrol.application.provider.sub.create;

import br.com.subscontrol.domain.provider.sub.SubProvider;

public record CreateSubProviderOutput(String id) {

    public static CreateSubProviderOutput from(final SubProvider provider) {
        return new CreateSubProviderOutput(provider.getId().getValue());
    }

}