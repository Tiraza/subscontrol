package br.com.subscontrol.infraestructure.provider.content.persistence;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.ProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.authentication.AuthenticationJpaEntity;
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
            final boolean active,
            final Instant lastSync,
            final AuthenticationJpaEntity authentication
    ) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.baseUrl = baseUrl;
        this.active = active;
        this.lastSync = lastSync;
        this.authentication = authentication;
    }

    public static ContentProviderJpaEntity from(final ContentProvider contentProvider) {
        return new ContentProviderJpaEntity(
                contentProvider.getId().getValue(),
                contentProvider.getType(),
                contentProvider.getName(),
                contentProvider.getBaseUrl(),
                contentProvider.isActive(),
                contentProvider.getLastSync(),
                AuthenticationJpaEntity.from(contentProvider.getAuthentication())
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
                Authentication.with(
                        getAuthentication().getProviderID(),
                        getAuthentication().getType().name(),
                        getAuthentication().getClientId(),
                        getAuthentication().getClientSecret(),
                        getAuthentication().getAuthorizationUrl(),
                        getAuthentication().getTokenUrl(),
                        getAuthentication().getFile()
                )
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
