package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.provider.Provider;
import br.com.subscontrol.domain.provider.authentication.Authentication;

import java.time.Instant;
import java.util.Objects;

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
        this.type = Objects.requireNonNull(type, "'type' should not be null");;
        selfValidate();
    }

    public static SubProvider create(final SubProviderType type, final String name, final String baseUrl, final Authentication authentication) {
        return new SubProvider(SubProviderID.unique(), type, name, baseUrl, true, null, authentication);
    }

    public static SubProvider create(
            final String type,
            final String name,
            final String baseUrl,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        Authentication authentication = Authentication.with(clientId, clientSecret, authorizationUrl, tokenUrl);
        return new SubProvider(SubProviderID.unique(), SubProviderType.from(type), name, baseUrl, true, null, authentication);
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

    public static SubProvider with(final SubProvider provider) {
        return new SubProvider(
                provider.getId(),
                provider.getType(),
                provider.getName(),
                provider.getBaseUrl(),
                provider.isActive(),
                provider.getLastSync(),
                provider.getAuthentication()
        );
    }

    public void update(
            final String name,
            final String baseUrl,
            final boolean isActive,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        Authentication authentication = Authentication.with(clientId, clientSecret, authorizationUrl, tokenUrl);
        update(name, baseUrl, isActive, authentication);
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
