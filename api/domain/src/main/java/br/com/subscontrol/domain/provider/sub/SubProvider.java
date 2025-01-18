package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.provider.Provider;
import br.com.subscontrol.domain.provider.authentication.Authentication;

import java.time.Instant;
import java.util.Objects;

public class SubProvider extends Provider<SubProviderID> {

    private final SubProviderType type;

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

    public static SubProvider create(
            String type,
            String name,
            String baseUrl,
            String authenticationType,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl,
            String fileBase64) {
        SubProviderID providerID = SubProviderID.unique();
        return new SubProvider(
                providerID,
                SubProviderType.from(type),
                name,
                baseUrl,
                true,
                null,
                Authentication.create(
                        providerID,
                        authenticationType,
                        clientId,
                        clientSecret,
                        authorizationUrl,
                        tokenUrl,
                        fileBase64
                ));
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

    public void update(final String name, final String baseUrl, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.baseUrl = baseUrl;
        selfValidate();
    }

    public SubProviderType getType() {
        return type;
    }
}
