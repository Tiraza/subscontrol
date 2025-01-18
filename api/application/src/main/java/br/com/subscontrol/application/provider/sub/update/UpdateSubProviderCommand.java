package br.com.subscontrol.application.provider.sub.update;

public record UpdateSubProviderCommand(
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

    public static UpdateSubProviderCommand with(
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
        return new UpdateSubProviderCommand(
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
