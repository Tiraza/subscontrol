package br.com.subscontrol.infraestructure.provider.content.models;

import br.com.subscontrol.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.Base64;
import java.util.UUID;

@JacksonTest
class CreateContentProviderRequestTest {

    @Autowired
    private JacksonTester<CreateContentProviderRequest> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedAuthenticationType = "CLIENT_SECRET";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedFile = Base64.getEncoder().encodeToString("Test".getBytes());

        final var response = new CreateContentProviderRequest(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                expectedFile
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.base_url", expectedBaseUrl)
                .hasJsonPathValue("$.authentication_type", expectedAuthenticationType)
                .hasJsonPathValue("$.client_id", expectedClientId)
                .hasJsonPathValue("$.client_secret", expectedClientSecret)
                .hasJsonPathValue("$.authorization_url", expectedAuthorizationUrl)
                .hasJsonPathValue("$.token_url", expectedTokenUrl)
                .hasJsonPathValue("$.file", expectedFile);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedAuthenticationType = "CLIENT_SECRET";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedFile = Base64.getEncoder().encodeToString("Test".getBytes());

        final var json = """
        {
            "type": "%s",
            "name": "%s",
            "base_url": "%s",
            "authentication_type": "%s",
            "client_id": "%s",
            "client_secret": "%s",
            "authorization_url": "%s",
            "token_url": "%s",
            "file": "%s"
        }
        """.formatted(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                expectedFile
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("type", expectedType)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("baseUrl", expectedBaseUrl)
                .hasFieldOrPropertyWithValue("authenticationType", expectedAuthenticationType)
                .hasFieldOrPropertyWithValue("clientId", expectedClientId)
                .hasFieldOrPropertyWithValue("clientSecret", expectedClientSecret)
                .hasFieldOrPropertyWithValue("authorizationUrl", expectedAuthorizationUrl)
                .hasFieldOrPropertyWithValue("tokenUrl", expectedTokenUrl)
                .hasFieldOrPropertyWithValue("fileBase64", expectedFile);
    }
}
