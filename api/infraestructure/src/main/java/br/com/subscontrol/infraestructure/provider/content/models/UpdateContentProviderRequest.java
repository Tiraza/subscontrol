package br.com.subscontrol.infraestructure.provider.content.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateContentProviderRequest(
        @JsonProperty("name") String name,
        @JsonProperty("base_url") String baseUrl,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("authentication_type") String authenticationType,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("client_secret") String clientSecret,
        @JsonProperty("authorization_url") String authorizationUrl,
        @JsonProperty("token_url") String tokenUrl,
        @JsonProperty("file") String fileBase64
) {
}
