package br.com.subscontrol.infraestructure.provider.sub.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateSubProviderRequest(
        @JsonProperty("type") String type,
        @JsonProperty("name") String name,
        @JsonProperty("base_url") String baseUrl,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("client_secret") String clientSecret,
        @JsonProperty("authorization_url") String authorizationUrl,
        @JsonProperty("token_url") String tokenUrl
) {

}
