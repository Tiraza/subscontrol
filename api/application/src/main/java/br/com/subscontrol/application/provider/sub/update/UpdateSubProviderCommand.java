package br.com.subscontrol.application.provider.sub.update;

public record UpdateSubProviderCommand(
        String id,
        String name,
        String baseUrl,
        boolean active,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl
) {

    public static UpdateSubProviderCommand with(
            String id,
            String name,
            String baseUrl,
            boolean active,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl) {
        return new UpdateSubProviderCommand(id, name, baseUrl, active, clientId, clientSecret, authorizationUrl, tokenUrl);
    }

}
