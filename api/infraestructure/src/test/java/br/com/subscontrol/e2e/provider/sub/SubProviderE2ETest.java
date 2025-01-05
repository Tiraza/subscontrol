package br.com.subscontrol.e2e.provider.sub;

import br.com.subscontrol.E2ETest;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.infraestructure.configuration.json.Json;
import br.com.subscontrol.infraestructure.provider.sub.models.CreateSubProviderRequest;
import br.com.subscontrol.infraestructure.provider.sub.models.SubProviderResponse;
import br.com.subscontrol.infraestructure.provider.sub.models.UpdateSubProviderRequest;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
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
class SubProviderE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SubProviderRepository repository;

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
    void asAdminIShouldBeAbleToCreateANewSubProviderWithValidValues() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = true;

        final var subProviderID = givenASubProvider(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        assertEquals(1, repository.count());

        final var subProvider = repository.findById(subProviderID.getValue()).get();

        assertEquals(expectedType, subProvider.getType().getName());
        assertEquals(expectedName, subProvider.getName());
        assertEquals(expectedBaseUrl, subProvider.getBaseUrl());
        assertEquals(expectedClientId, subProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, subProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, subProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, subProvider.getAuthentication().tokenUrl());
        assertEquals(expectedIsActive, subProvider.isActive());
        assertNull(subProvider.getLastSync());
    }

    @Test
    void asAdminIShouldBeAbleToNavigateToAllSubProviders() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        givenASubProvider("Patreon");
        givenASubProvider("Kickstarter");
        givenASubProvider("Indiegogo");

        assertEquals(3, repository.count());

        listSubProviders(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Indiegogo")));

        listSubProviders(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Kickstarter")));

        listSubProviders(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Patreon")));

        listSubProviders(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void asAdminIShouldBeAbleToSearchBetweenAllSubProviders() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        givenASubProvider("Patreon");
        givenASubProvider("Kickstarter");
        givenASubProvider("Indiegogo");

        assertEquals(3, repository.count());

        listSubProviders(0, 3, "kick")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Kickstarter")));
    }

    @Test
    void asAdminIShouldBeAbleToSortAllSubProvidersByNameDesc() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        givenASubProvider("Patreon");
        givenASubProvider("Kickstarter");
        givenASubProvider("Indiegogo");

        assertEquals(3, repository.count());

        listSubProviders(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Patreon")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Kickstarter")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Indiegogo")));
    }

    @Test
    void asAdminIShouldBeAbleToGetASubProviderByItsIdentifier() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = true;

        final var subProviderID = givenASubProvider(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        assertEquals(1, repository.count());

        final var subProvider = retrieveASubProvider(subProviderID.getValue());

        assertEquals(expectedType, subProvider.type());
        assertEquals(expectedName, subProvider.name());
        assertEquals(expectedBaseUrl, subProvider.baseUrl());
        assertEquals(expectedClientId, subProvider.clientId());
        assertEquals(expectedClientSecret, subProvider.clientSecret());
        assertEquals(expectedAuthorizationUrl, subProvider.authorizationUrl());
        assertEquals(expectedTokenUrl, subProvider.tokenUrl());
        assertEquals(expectedIsActive, subProvider.active());
        assertNull(subProvider.lastSync());
    }

    @Test
    void asAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundSubProvider() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var request = get("/subproviders/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("SubProvider with ID 123 was not found")));
    }

    @Test
    void asAdminIShouldBeAbleToUpdateASubProviderByItsIdentifier() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var subProviderID = givenASubProvider(
                "Patreon",
                "Integration",
                "http://www.test.com",
                "123",
                "123",
                "/test",
                "/test"
        );

        assertEquals(1, repository.count());

        final var expectedType = "Patreon";
        final var expectedName = "Patreon Provider";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = false;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var requestBody = new UpdateSubProviderRequest(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var request = put("/subproviders/" + subProviderID.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        this.mvc.perform(request).andExpect(status().isOk());

        final var subProvider = repository.findById(subProviderID.getValue()).get();

        assertEquals(expectedType, subProvider.getType().getName());
        assertEquals(expectedName, subProvider.getName());
        assertEquals(expectedBaseUrl, subProvider.getBaseUrl());
        assertEquals(expectedClientId, subProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, subProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, subProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, subProvider.getAuthentication().tokenUrl());
        assertEquals(expectedIsActive, subProvider.isActive());
        assertNull(subProvider.getLastSync());
    }

    @Test
    void asAdminIShouldBeAbleToDeleteASubProviderByItsIdentifier() throws Exception {
        assertTrue(POSTGRESQL_CONTAINER.isRunning());
        assertEquals(0, repository.count());

        final var subProviderID = givenASubProvider(
                "Patreon",
                "Patreon Integration",
                "http://www.patreon.com",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token"
        );

        assertEquals(1, repository.count());

        this.mvc.perform(delete("/subproviders/" + subProviderID.getValue()))
                .andExpect(status().isNoContent());

        assertEquals(0, repository.count());
    }

    private void givenASubProvider(final String name) throws Exception {
        givenASubProvider(
                "Patreon",
                name,
                "http://www.patreon.com",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token"
        );
    }

    private SubProviderID givenASubProvider(
            final String type,
            final String name,
            final String baseUrl,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl
    ) throws Exception {
        final var requestBody =
                new CreateSubProviderRequest(type, name, baseUrl, clientId, clientSecret, authorizationUrl, tokenUrl);

        final var request = post("/subproviders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        final var actualId = this.mvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/subproviders/", "");

        return SubProviderID.from(actualId);
    }

    private SubProviderResponse retrieveASubProvider(final String id) throws Exception {
        final var request = get("/subproviders/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, SubProviderResponse.class);
    }

    private ResultActions listSubProviders(final int page, final int perPage) throws Exception {
        return listSubProviders(page, perPage, "", "", "");
    }

    private ResultActions listSubProviders(final int page, final int perPage, final String search) throws Exception {
        return listSubProviders(page, perPage, search, "", "");
    }

    private ResultActions listSubProviders(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var request = get("/subproviders")
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
