package br.com.subscontrol.infraestructure.provider.content.persistence;

import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.ProviderJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "CONTENT_PROVIDERS")
public class ContentProviderJpaEntity extends ProviderJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContentProviderType type;

    public ContentProviderJpaEntity() {}

    private ContentProviderJpaEntity(
            final String id,
            final ContentProviderType type,
            final String name,
            final String baseUrl,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl,
            final boolean active,
            final Instant lastSync) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.baseUrl = baseUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
        this.active = active;
        this.lastSync = lastSync;
    }

    public static ContentProviderJpaEntity from(final ContentProvider contentProvider) {
        return new ContentProviderJpaEntity(
                contentProvider.getId().getValue(),
                contentProvider.getType(),
                contentProvider.getName(),
                contentProvider.getBaseUrl(),
                contentProvider.getAuthentication().clientId(),
                contentProvider.getAuthentication().clientSecret(),
                contentProvider.getAuthentication().authorizationUrl(),
                contentProvider.getAuthentication().tokenUrl(),
                contentProvider.isActive(),
                contentProvider.getLastSync()
        );
    }

    public ContentProvider toDomain() {
        return ContentProvider.with(
                getId(),
                getType(),
                getName(),
                getBaseUrl(),
                isActive(),
                getLastSync(),
                getAuthentication()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContentProviderType getType() {
        return type;
    }

    public void setType(ContentProviderType type) {
        this.type = type;
    }
}
