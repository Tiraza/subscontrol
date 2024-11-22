package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.provider.Provider;
import br.com.subscontrol.domain.provider.authentication.Authentication;

import java.time.Instant;

public class SubProvider extends Provider<SubProviderID> {

    protected SubProvider(
            final SubProviderID id,
            final String name,
            final String baseUrl,
            final boolean active,
            final Instant lastSync,
            final Authentication authentication) {
        super(id, name, baseUrl, active, lastSync, authentication);
        selfValidate();
    }

}
