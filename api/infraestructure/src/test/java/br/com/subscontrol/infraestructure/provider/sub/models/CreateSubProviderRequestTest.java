package br.com.subscontrol.infraestructure.provider.sub.models;

import br.com.subscontrol.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.UUID;

@JacksonTest
class CreateSubProviderRequestTest {

    @Autowired
    private JacksonTester<CreateSubProviderRequest> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var response = new CreateSubProviderRequest(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.base_url", expectedBaseUrl)
                .hasJsonPathValue("$.client_id", expectedClientId)
                .hasJsonPathValue("$.client_secret", expectedClientSecret)
                .hasJsonPathValue("$.authorization_url", expectedAuthorizationUrl)
                .hasJsonPathValue("$.token_url", expectedTokenUrl);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var json = """
        {
            "type": "%s",
            "name": "%s",
            "base_url": "%s",
            "client_id": "%s",
            "client_secret": "%s",
            "authorization_url": "%s",
            "token_url": "%s"
        }
        """.formatted(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("type", expectedType)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("baseUrl", expectedBaseUrl)
                .hasFieldOrPropertyWithValue("clientId", expectedClientId)
                .hasFieldOrPropertyWithValue("clientSecret", expectedClientSecret)
                .hasFieldOrPropertyWithValue("authorizationUrl", expectedAuthorizationUrl)
                .hasFieldOrPropertyWithValue("tokenUrl", expectedTokenUrl);
    }

}
