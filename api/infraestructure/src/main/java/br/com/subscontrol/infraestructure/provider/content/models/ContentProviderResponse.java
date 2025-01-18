package br.com.subscontrol.infraestructure.provider.content.models;

import br.com.subscontrol.application.provider.content.retrieve.get.ContentProviderOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ContentProviderResponse(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("name") String name,
        @JsonProperty("base_url") String baseUrl,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("last_sync") Instant lastSync,
        @JsonProperty("authentication_type") String authenticationType,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("client_secret") String clientSecret,
        @JsonProperty("authorization_url") String authorizationUrl,
        @JsonProperty("token_url") String tokenUrl
) {

    public static ContentProviderResponse from(final ContentProviderOutput output) {
        return new ContentProviderResponse(
                output.id(),
                output.type(),
                output.name(),
                output.baseUrl(),
                output.isActive(),
                output.lastSync(),
                output.authenticationType(),
                output.clientId(),
                output.clientSecret(),
                output.authorizationUrl(),
                output.tokenUrl()
        );
    }

}
