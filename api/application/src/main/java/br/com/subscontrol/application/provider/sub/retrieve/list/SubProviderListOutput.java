package br.com.subscontrol.application.provider.sub.retrieve.list;

import br.com.subscontrol.domain.provider.sub.SubProvider;

import java.time.Instant;

public record SubProviderListOutput(
        String id,
        String name,
        boolean isActive,
        Instant lastSync
) {

    public static SubProviderListOutput from (final SubProvider provider) {
        return new SubProviderListOutput(
                provider.getId().getValue(),
                provider.getName(),
                provider.isActive(),
                provider.getLastSync()
        );
    }

}

