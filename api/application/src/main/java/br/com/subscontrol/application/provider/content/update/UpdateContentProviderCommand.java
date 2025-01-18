package br.com.subscontrol.application.provider.content.update;

public record UpdateContentProviderCommand(
        String id,
        String name,
        String baseUrl,
        boolean active,
        String authenticationType,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl,
        String fileBase64
) {

    public static UpdateContentProviderCommand with(
            String id,
            String name,
            String baseUrl,
            boolean active,
            String authenticationType,
            String clientId,
            String clientSecret,
            String authorizationUrl,
            String tokenUrl,
            String fileBase64) {
        return new UpdateContentProviderCommand(
                id,
                name,
                baseUrl,
                active,
                authenticationType,
                clientId,
                clientSecret,
                authorizationUrl,
                tokenUrl,
                fileBase64
        );
    }

}
