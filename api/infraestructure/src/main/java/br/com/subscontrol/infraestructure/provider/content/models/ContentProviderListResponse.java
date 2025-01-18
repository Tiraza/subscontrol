package br.com.subscontrol.infraestructure.provider.content.models;

import br.com.subscontrol.application.provider.content.retrieve.list.ContentProviderListOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ContentProviderListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("last_sync") Instant lastSync
) {

    public static ContentProviderListResponse from(final ContentProviderListOutput output) {
        return new ContentProviderListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.lastSync()
        );
    }

}
