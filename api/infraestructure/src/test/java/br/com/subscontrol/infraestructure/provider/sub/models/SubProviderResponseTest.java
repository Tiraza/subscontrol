package br.com.subscontrol.infraestructure.provider.sub.models;

import br.com.subscontrol.JacksonTest;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.utils.InstantUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.UUID;

@JacksonTest
class SubProviderResponseTest {

    @Autowired
    private JacksonTester<SubProviderResponse> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedLastSync = InstantUtils.now();
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var response = new SubProviderResponse(
                expectedId,
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedLastSync,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.base_url", expectedBaseUrl)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.last_sync", expectedLastSync.toString())
                .hasJsonPathValue("$.authentication_type", expectedAuthenticationType)
                .hasJsonPathValue("$.client_id", expectedClientId)
                .hasJsonPathValue("$.client_secret", expectedClientSecret)
                .hasJsonPathValue("$.authorization_url", expectedAuthorizationUrl)
                .hasJsonPathValue("$.token_url", expectedTokenUrl);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedLastSync = InstantUtils.now();
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var json = """
        {
            "id": "%s",
            "type": "%s",
            "name": "%s",
            "base_url": "%s",
            "is_active": %s,
            "last_sync": "%s",
            "authentication_type": "%s",
            "client_id": "%s",
            "client_secret": "%s",
            "authorization_url": "%s",
            "token_url": "%s"
        }
        """.formatted(
                expectedId,
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedLastSync.toString(),
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("type", expectedType)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("baseUrl", expectedBaseUrl)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("lastSync", expectedLastSync)
                .hasFieldOrPropertyWithValue("authenticationType", expectedAuthenticationType)
                .hasFieldOrPropertyWithValue("clientId", expectedClientId)
                .hasFieldOrPropertyWithValue("clientSecret", expectedClientSecret)
                .hasFieldOrPropertyWithValue("authorizationUrl", expectedAuthorizationUrl)
                .hasFieldOrPropertyWithValue("tokenUrl", expectedTokenUrl);
    }

}
