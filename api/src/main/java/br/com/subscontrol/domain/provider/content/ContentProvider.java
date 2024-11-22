package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.provider.Provider;
import br.com.subscontrol.domain.provider.authentication.Authentication;

import java.time.Instant;

public class ContentProvider extends Provider<ContentProviderID> {

    public ContentProvider(
            final ContentProviderID id,
            final String name,
            final String baseUrl,
            final boolean active,
            final Instant lastSync,
            final Authentication authentication) {
        super(id, name, baseUrl, active, lastSync, authentication);
        selfValidate();
    }

}
