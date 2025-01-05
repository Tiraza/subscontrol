package br.com.subscontrol.infraestructure.provider.sub.models;

import br.com.subscontrol.application.provider.sub.retrieve.get.SubProviderOutPut;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SubProviderResponse(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("name") String name,
        @JsonProperty("base_url") String baseUrl,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("last_sync") Instant lastSync,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("client_secret") String clientSecret,
        @JsonProperty("authorization_url") String authorizationUrl,
        @JsonProperty("token_url") String tokenUrl
) {

    public static SubProviderResponse from(final SubProviderOutPut output) {
        return new SubProviderResponse(
                output.id(),
                output.type(),
                output.name(),
                output.baseUrl(),
                output.isActive(),
                output.lastSync(),
                output.clientId(),
                output.clientSecret(),
                output.authorizationUrl(),
                output.tokenUrl()
        );
    }

}
