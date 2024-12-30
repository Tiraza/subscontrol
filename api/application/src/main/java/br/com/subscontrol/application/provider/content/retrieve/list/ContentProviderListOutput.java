package br.com.subscontrol.application.provider.content.retrieve.list;

import br.com.subscontrol.domain.provider.content.ContentProvider;

import java.time.Instant;

public record ContentProviderListOutput(
        String id,
        String name,
        boolean isActive,
        Instant lastSync
) {

    public static ContentProviderListOutput from(final ContentProvider provider) {
        return new ContentProviderListOutput(
                provider.getId().getValue(),
                provider.getName(),
                provider.isActive(),
                provider.getLastSync()
        );
    }
}
