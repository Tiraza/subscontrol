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
            final ContentProviderType type,
            final String name,
            final String baseUrl,
            final Authentication authentication) {
        return new ContentProvider(ContentProviderID.unique(), type, name, baseUrl, true, null, authentication);
    }

    public static ContentProvider create(
            final String type,
            final String name,
            final String baseUrl,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        Authentication authentication = Authentication.with(clientId, clientSecret, authorizationUrl, tokenUrl);
        return new ContentProvider(ContentProviderID.unique(), ContentProviderType.from(type), name, baseUrl, true, null, authentication);
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

    public ContentProviderType getType() {
        return type;
    }
}
