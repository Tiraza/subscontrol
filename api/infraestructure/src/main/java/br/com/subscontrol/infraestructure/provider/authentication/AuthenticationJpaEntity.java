package br.com.subscontrol.infraestructure.provider.authentication;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import jakarta.persistence.*;

@Entity
@Table(name = "AUTHENTICATIONS")
public class AuthenticationJpaEntity {

    @Id
    @Column(name = "provider_id", nullable = false)
    private String providerID;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AuthenticationType type;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "authorization_url")
    private String authorizationUrl;

    @Column(name = "token_url")
    private String tokenUrl;

    @Column(name = "file")
    private byte[] file;

    public AuthenticationJpaEntity() {}

    public AuthenticationJpaEntity(
            final String providerID,
            final AuthenticationType type,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl,
            final byte[] file) {
        this.providerID = providerID;
        this.type = type;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
        this.file = file;
    }

    public static AuthenticationJpaEntity from(final Authentication authentication) {
        return new AuthenticationJpaEntity(
                authentication.getProviderID().getValue(),
                authentication.getType(),
                authentication.getClientId(),
                authentication.getClientSecret(),
                authentication.getAuthorizationUrl(),
                authentication.getTokenUrl(),
                authentication.getFile()
        );
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public AuthenticationType getType() {
        return type;
    }

    public void setType(AuthenticationType type) {
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
