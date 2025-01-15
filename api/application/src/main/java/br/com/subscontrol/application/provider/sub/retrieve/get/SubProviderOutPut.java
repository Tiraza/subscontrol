package br.com.subscontrol.application.provider.sub.retrieve.get;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;

import java.time.Instant;

public record SubProviderOutPut(
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

    public static SubProviderOutPut from(final SubProvider provider) {
        final Authentication authentication = provider.getAuthentication();
        return new SubProviderOutPut(
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