package br.com.subscontrol.application.provider.content.create;

public record CreateContentProviderCommand(
        String type,
        String name,
        String baseUrl,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl
) {

    public static CreateContentProviderCommand with(
            String type,
            String name,
            String baseUrl,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl) {
        return new CreateContentProviderCommand(type, name, baseUrl, clientId, clientSecret, authorizationUrl, tokenUrl);
    }

}
