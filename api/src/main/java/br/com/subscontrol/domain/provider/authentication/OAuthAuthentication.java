package br.com.subscontrol.domain.provider.authentication;

public class OAuthAuthentication extends Authentication {

    private String clientId;
    private String clientSecret;
    private String authorizationUrl;
    private String tokenUrl;

    public OAuthAuthentication(String clientId, String clientSecret, String authorizationUrl, String tokenUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationUrl = authorizationUrl;
        this.tokenUrl = tokenUrl;
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
}
