package br.com.subscontrol.infraestructure.provider.sub.persistence;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.infraestructure.provider.ProviderJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

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

    public static SubProviderJpaEntity from(final SubProvider subProvider) {
        String clienteId = "";
        String clienteSecret = "";
        String authorizationUrl = "";
        String tokenUrl = "";

        Authentication authentication = subProvider.getAuthentication();
        if (!Objects.isNull(authentication)) {
           clienteId = authentication.clientId();
           clienteSecret = authentication.clientSecret();
           authorizationUrl = authentication.authorizationUrl();
           tokenUrl = authentication.tokenUrl();
        }

        return new SubProviderJpaEntity(
                subProvider.getId().getValue(),
                subProvider.getType(),
                subProvider.getName(),
                subProvider.getBaseUrl(),
                clienteId,
                clienteSecret,
                authorizationUrl,
                tokenUrl,
                subProvider.isActive(),
                subProvider.getLastSync()
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
                getAuthentication()
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
