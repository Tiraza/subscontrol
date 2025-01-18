package br.com.subscontrol.infraestructure.provider.content.models;

import br.com.subscontrol.JacksonTest;
import br.com.subscontrol.domain.utils.InstantUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class ContentProviderListResponseTest {

    @Autowired
    private JacksonTester<ContentProviderListResponse> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Google Integration";
        final var expectedIsActive = true;
        final var expectedLastSync = InstantUtils.now();

        final var response = new ContentProviderListResponse(
                expectedId,
                expectedName,
                expectedIsActive,
                expectedLastSync
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.last_sync", expectedLastSync.toString());
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Google Integration";
        final var expectedIsActive = true;
        final var expectedLastSync = InstantUtils.now();

        final var json = """
        {
            "id": "%s",
            "name": "%s",
            "is_active": "%s",
            "last_sync": "%s"
        }
        """.formatted(
                expectedId,
                expectedName,
                expectedIsActive,
                expectedLastSync.toString()
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("lastSync", expectedLastSync);
    }
}
