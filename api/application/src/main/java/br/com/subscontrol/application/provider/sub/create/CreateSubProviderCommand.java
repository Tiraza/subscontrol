package br.com.subscontrol.application.provider.sub.create;

public record CreateSubProviderCommand(
        String type,
        String name,
        String baseUrl,
        String authenticationType,
        String clientId,
        String clientSecret,
        String authorizationUrl,
        String tokenUrl,
        String fileBase64
) {

    public static CreateSubProviderCommand with(
            final String type,
            final String name,
            final String baseUrl,
            final String authenticationType,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl,
            final String fileBase64) {
        return new CreateSubProviderCommand(
                type,
                name,
                baseUrl,
                authenticationType,
                clientId,
                clientSecret,
                authorizationUrl,
                tokenUrl,
                fileBase64);
    }

}