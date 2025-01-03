package br.com.subscontrol.infraestructure.provider.sub.models;

import br.com.subscontrol.application.provider.sub.retrieve.list.SubProviderListOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SubProviderListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("last_sync") Instant lastSync
) {

    public static SubProviderListResponse from(final SubProviderListOutput output) {
        return new SubProviderListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.lastSync()
        );
    }

}
