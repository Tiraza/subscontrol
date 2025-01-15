package br.com.subscontrol.application.provider.content.retrieve.get;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.content.ContentProvider;

import java.time.Instant;

public record ContentProviderOutput(
        String id,
        String type,
        String name,
        String baseUrl,
        boolean isActive,
        Instant lastSync,
        String authenticationType,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl
) {

    public static ContentProviderOutput from(final ContentProvider provider) {
        final Authentication authentication = provider.getAuthentication();
        return new ContentProviderOutput(
                provider.getId().getValue(),
                provider.getType().getName(),
                provider.getName(),
                provider.getBaseUrl(),
                provider.isActive(),
                provider.getLastSync(),
                authentication.getType().name(),
                authentication.getClientId(),
                authentication.getClientSecret(),
                authentication.getAuthorizationUrl(),
                authentication.getTokenUrl()
        );
    }
}
