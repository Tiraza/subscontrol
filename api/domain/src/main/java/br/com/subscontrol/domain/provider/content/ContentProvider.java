package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.provider.Provider;
import br.com.subscontrol.domain.provider.authentication.Authentication;

import java.time.Instant;

public class ContentProvider extends Provider<ContentProviderID> {

    private ContentProviderType type;

    protected ContentProvider(
            final ContentProviderID id,
            final ContentProviderType type,
            final String name,
            final String baseUrl,
            final boolean active,
            final Instant lastSync,
            final Authentication authentication) {
        super(id, name, baseUrl, active, lastSync, authentication);
        this.type = type;
        selfValidate();
    }

    public static ContentProvider create(
            String type,
            String name,
            String baseUrl,
            String authenticationType,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl,
            String fileBase64) {
        ContentProviderID providerID = ContentProviderID.unique();
        return new ContentProvider(
                providerID,
                ContentProviderType.from(type),
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

    public static ContentProvider with(
            final String id,
            final ContentProviderType type,
            final String name,
            final String baseUrl,
            final boolean active,
            final Instant lastSync,
            final Authentication authentication) {
        return new ContentProvider(ContentProviderID.from(id), type, name, baseUrl, active, lastSync, authentication);
    }

    public static ContentProvider with(final ContentProvider provider) {
        return new ContentProvider(
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

    public ContentProviderType getType() {
        return type;
    }

}
