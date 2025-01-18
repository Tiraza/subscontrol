package br.com.subscontrol.infraestructure.provider.sub.persistence;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.infraestructure.provider.ProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.authentication.AuthenticationJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "SUB_PROVIDERS")
public class SubProviderJpaEntity extends ProviderJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SubProviderType type;

    public SubProviderJpaEntity() {}

    private SubProviderJpaEntity(
            final String id,
            final SubProviderType type,
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

    public static SubProviderJpaEntity from(final SubProvider subProvider) {
        return new SubProviderJpaEntity(
                subProvider.getId().getValue(),
                subProvider.getType(),
                subProvider.getName(),
                subProvider.getBaseUrl(),
                subProvider.isActive(),
                subProvider.getLastSync(),
                AuthenticationJpaEntity.from(subProvider.getAuthentication())
        );
    }

    public SubProvider toDomain() {
        return SubProvider.with(
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

    public SubProviderType getType() {
        return type;
    }

    public void setType(SubProviderType type) {
        this.type = type;
    }
}
