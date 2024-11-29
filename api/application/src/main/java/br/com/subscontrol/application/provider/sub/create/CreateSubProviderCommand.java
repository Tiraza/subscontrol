package br.com.subscontrol.application.provider.sub.create;

public record CreateSubProviderCommand(
        String type,
        String name,
        String baseUrl,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl
) {

    public static CreateSubProviderCommand with(
            String type,
            String name,
            String baseUrl,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl) {
        return new CreateSubProviderCommand(type, name, baseUrl, clientId, clientSecret, authorizationUrl, tokenUrl);
    }

}