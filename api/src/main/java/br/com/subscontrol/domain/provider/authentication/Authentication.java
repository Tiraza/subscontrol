package br.com.subscontrol.domain.provider.authentication;

public record Authentication (
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl) {

    public static Authentication with(
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl) {
        return new Authentication(clientId, clientSecret, authorizationUrl, tokenUrl);
    }

}
