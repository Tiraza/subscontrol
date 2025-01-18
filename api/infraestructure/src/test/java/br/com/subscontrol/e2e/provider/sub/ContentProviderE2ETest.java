package br.com.subscontrol.e2e.provider.sub;

import br.com.subscontrol.E2ETest;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.infraestructure.configuration.json.Json;
import br.com.subscontrol.infraestructure.provider.content.models.ContentProviderResponse;
import br.com.subscontrol.infraestructure.provider.content.models.CreateContentProviderRequest;
import br.com.subscontrol.infraestructure.provider.content.models.UpdateContentProviderRequest;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
class ContentProviderE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ContentProviderRepository repository;

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("subs-control")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("postgresql.port", () -> POSTGRESQL_CONTAINER.getMappedPort(5432));
    }

    @Test
    void asAdminIShouldBeAbleToCreateANewContentProviderWithValidValues() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var expectedType = "Google Drive";
        final var expectedName = "Google Integration";
        final var expectedBaseUrl = "http://www.google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = true;

        final var subProviderID = givenASubProvider(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        assertEquals(1, repository.count());

        final var provider = repository.findById(subProviderID.getValue()).get();

        assertEquals(expectedType, provider.getType().getName());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedBaseUrl, provider.getBaseUrl());
        assertEquals(expectedAuthenticationType, provider.getAuthentication().getType());
        assertEquals(expectedClientId, provider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, provider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, provider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, provider.getAuthentication().getTokenUrl());
        assertEquals(expectedIsActive, provider.isActive());
        assertNull(provider.getLastSync());
    }

    @Test
    void asAdminIShouldBeAbleToNavigateToAllContentProviders() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        givenASubProvider("Google");
        givenASubProvider("Dropbox");
        givenASubProvider("OneDrive");

        assertEquals(3, repository.count());

        listcontentproviders(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Dropbox")));

        listcontentproviders(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Google")));

        listcontentproviders(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("OneDrive")));

        listcontentproviders(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void asAdminIShouldBeAbleToSearchBetweenAllContentProviders() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        givenASubProvider("Google");
        givenASubProvider("Dropbox");
        givenASubProvider("OneDrive");

        assertEquals(3, repository.count());

        listcontentproviders(0, 3, "pbox")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Dropbox")));
    }

    @Test
    void asAdminIShouldBeAbleToSortAllContentProvidersByNameDesc() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        givenASubProvider("Google");
        givenASubProvider("Dropbox");
        givenASubProvider("OneDrive");

        assertEquals(3, repository.count());

        listcontentproviders(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("OneDrive")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Google")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Dropbox")));
    }

    @Test
    void asAdminIShouldBeAbleToGetAContentProviderByItsIdentifier() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var expectedType = "Google Drive";
        final var expectedName = "Google Integration";
        final var expectedBaseUrl = "http://www.google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = true;

        final var providerID = givenASubProvider(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        assertEquals(1, repository.count());

        final var provider = retrieveASubProvider(providerID.getValue());

        assertEquals(expectedType, provider.type());
        assertEquals(expectedName, provider.name());
        assertEquals(expectedBaseUrl, provider.baseUrl());
        assertEquals(expectedAuthenticationType, provider.authenticationType());
        assertEquals(expectedClientId, provider.clientId());
        assertEquals(expectedClientSecret, provider.clientSecret());
        assertEquals(expectedAuthorizationUrl, provider.authorizationUrl());
        assertEquals(expectedTokenUrl, provider.tokenUrl());
        assertEquals(expectedIsActive, provider.active());
        assertNull(provider.lastSync());
    }

    @Test
    void asAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundContentProvider() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var request = get("/contentproviders/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("ContentProvider with ID 123 was not found")));
    }

    @Test
    void asAdminIShouldBeAbleToUpdateAContentProviderByItsIdentifier() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var subProviderID = givenASubProvider(
                "Google Drive",
                "Integration",
                "http://www.test.com",
                "CLIENT_SECRET",
                "123",
                "123",
                "/test",
                "/test",
                null
        );

        assertEquals(1, repository.count());

        final var expectedType = "Google Drive";
        final var expectedName = "Google Provider";
        final var expectedBaseUrl = "http://www.google.com";
        final var expectedIsActive = false;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var requestBody = new UpdateContentProviderRequest(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var request = put("/contentproviders/" + subProviderID.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        this.mvc.perform(request).andExpect(status().isOk());

        final var provider = repository.findById(subProviderID.getValue()).get();

        assertEquals(expectedType, provider.getType().getName());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedBaseUrl, provider.getBaseUrl());
        assertEquals(expectedAuthenticationType, provider.getAuthentication().getType());
        assertEquals(expectedClientId, provider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, provider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, provider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, provider.getAuthentication().getTokenUrl());
        assertEquals(expectedIsActive, provider.isActive());
        assertNull(provider.getLastSync());
    }

    @Test
    void asAdminIShouldBeAbleToDeleteAContentProviderByItsIdentifier() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var providerID = givenASubProvider(
                "Google Drive",
                "Google Integration",
                "http://www.google.com",
                "CLIENT_SECRET",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );

        assertEquals(1, repository.count());

        this.mvc.perform(delete("/contentproviders/" + providerID.getValue()))
                .andExpect(status().isNoContent());

        assertEquals(0, repository.count());
    }

    private void givenASubProvider(final String name) throws Exception {
        givenASubProvider(
                "Google Drive",
                name,
                "http://www.google.com",
                "CLIENT_SECRET",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );
    }

    private ContentProviderID givenASubProvider(
            final String type,
            final String name,
            final String baseUrl,
            final String authenticationType,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl,
            final String file
    ) throws Exception {
        final var requestBody = new CreateContentProviderRequest(
                type,
                name,
                baseUrl,
                authenticationType,
                clientId,
                clientSecret,
                authorizationUrl,
                tokenUrl,
                file
        );

        final var request = post("/contentproviders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        final var actualId = this.mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/contentproviders/", "");

        return ContentProviderID.from(actualId);
    }

    private ContentProviderResponse retrieveASubProvider(final String id) throws Exception {
        final var request = get("/contentproviders/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, ContentProviderResponse.class);
    }

    private ResultActions listcontentproviders(final int page, final int perPage) throws Exception {
        return listcontentproviders(page, perPage, "", "", "");
    }

    private ResultActions listcontentproviders(final int page, final int perPage, final String search) throws Exception {
        return listcontentproviders(page, perPage, search, "", "");
    }

    private ResultActions listcontentproviders(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var request = get("/contentproviders")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(request);
    }

}
