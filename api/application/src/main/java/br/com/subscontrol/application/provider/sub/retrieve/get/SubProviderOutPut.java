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
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl
) {

    public static SubProviderOutPut from(final SubProvider provider) {
        String clientId = "";
        String clientSecret = "";
        String authorizationUrl = "";
        String tokenUrl = "";

        Authentication authentication = provider.getAuthentication();
        if (authentication != null) {
            clientId = authentication.getClientId();
            clientSecret = authentication.getClientSecret();
            authorizationUrl = authentication.getAuthorizationUrl();
            tokenUrl = authentication.getTokenUrl();
        }

        return new SubProviderOutPut(
                provider.getId().getValue(),
                provider.getType().getName(),
                provider.getName(),
                provider.getBaseUrl(),
                provider.isActive(),
                provider.getLastSync(),
                clientId,
                clientSecret,
                authorizationUrl,
                tokenUrl
        );
    }

}