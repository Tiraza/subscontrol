package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.provider.Provider;
import br.com.subscontrol.domain.provider.authentication.Authentication;

import java.time.Instant;

public class SubProvider extends Provider<SubProviderID> {

    private SubProviderType type;

    protected SubProvider(
            final SubProviderID id,
            final SubProviderType type,
            final String name,
            final String baseUrl,
            final boolean active,
            final Instant lastSync,
            final Authentication authentication) {
        super(id, name, baseUrl, active, lastSync, authentication);
        this.type = type;
        selfValidate();
    }

    public static SubProvider create(final SubProviderType type, final String name, final String baseUrl, final Authentication authentication) {
        return new SubProvider(SubProviderID.unique(), type, name, baseUrl, true, null, authentication);
    }

    public static SubProvider with(
            final String id,
            final SubProviderType type,
            final String name,
            final String baseUrl,
            final boolean active,
            final Instant lastSync,
            final Authentication authentication) {
        return new SubProvider(SubProviderID.from(id), type, name, baseUrl, active, lastSync, authentication);
    }

    public void update(final String name, final String baseUrl, final boolean isActive, final Authentication authentication) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.baseUrl = baseUrl;
        this.authentication = authentication;
        selfValidate();
    }

    public SubProviderType getType() {
        return type;
    }
}
