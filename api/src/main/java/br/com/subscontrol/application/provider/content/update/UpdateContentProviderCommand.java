package br.com.subscontrol.application.provider.content.update;

public record UpdateContentProviderCommand(
        String id,
        String name,
        String baseUrl,
        boolean active,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl
) {

    public static UpdateContentProviderCommand with(
            String id,
            String name,
            String baseUrl,
            boolean active,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl) {
        return new UpdateContentProviderCommand(id, name, baseUrl, active, clientId, clientSecret, authorizationUrl, tokenUrl);
    }

}
