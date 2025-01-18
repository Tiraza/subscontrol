package br.com.subscontrol.infraestructure.provider.sub.models;

import br.com.subscontrol.JacksonTest;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.Base64;
import java.util.UUID;

@JacksonTest
class UpdateSubProviderRequestTest {

    @Autowired
    private JacksonTester<UpdateSubProviderRequest> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedFile = Base64.getEncoder().encodeToString("Test".getBytes());

        final var response = new UpdateSubProviderRequest(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                expectedFile
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.base_url", expectedBaseUrl)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.authentication_type", expectedAuthenticationType)
                .hasJsonPathValue("$.client_id", expectedClientId)
                .hasJsonPathValue("$.client_secret", expectedClientSecret)
                .hasJsonPathValue("$.authorization_url", expectedAuthorizationUrl)
                .hasJsonPathValue("$.token_url", expectedTokenUrl)
                .hasJsonPathValue("$.file", expectedFile);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedFile = Base64.getEncoder().encodeToString("Test".getBytes());

        final var json = """
        {
            "name": "%s",
            "base_url": "%s",
            "is_active": %s,
            "authentication_type": "%s",
            "client_id": "%s",
            "client_secret": "%s",
            "authorization_url": "%s",
            "token_url": "%s",
            "file": "%s"
        }
        """.formatted(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                expectedFile
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("baseUrl", expectedBaseUrl)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("authenticationType", expectedAuthenticationType)
                .hasFieldOrPropertyWithValue("clientId", expectedClientId)
                .hasFieldOrPropertyWithValue("clientSecret", expectedClientSecret)
                .hasFieldOrPropertyWithValue("authorizationUrl", expectedAuthorizationUrl)
                .hasFieldOrPropertyWithValue("tokenUrl", expectedTokenUrl)
                .hasFieldOrPropertyWithValue("fileBase64", expectedFile);
    }

}
