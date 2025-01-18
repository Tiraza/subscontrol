package br.com.subscontrol.infraestructure.api;

import br.com.subscontrol.ControllerTest;
import br.com.subscontrol.application.provider.content.create.CreateContentProviderOutput;
import br.com.subscontrol.application.provider.content.create.CreateContentProviderUseCase;
import br.com.subscontrol.application.provider.content.delete.DeleteContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.get.ContentProviderOutput;
import br.com.subscontrol.application.provider.content.retrieve.get.GetContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.list.ContentProviderListOutput;
import br.com.subscontrol.application.provider.content.retrieve.list.ListContentProviderUseCase;
import br.com.subscontrol.application.provider.content.update.UpdateContentProviderOutput;
import br.com.subscontrol.application.provider.content.update.UpdateContentProviderUseCase;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.infraestructure.provider.sub.models.CreateSubProviderRequest;
import br.com.subscontrol.infraestructure.provider.sub.models.UpdateSubProviderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = ContentProviderAPI.class)
class ContentProviderAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateContentProviderUseCase createContentProviderUseCase;

    @MockBean
    private GetContentProviderUseCase getContentProviderUseCase;

    @MockBean
    private UpdateContentProviderUseCase updateContentProviderUseCase;

    @MockBean
    private DeleteContentProviderUseCase deleteContentProviderUseCase;

    @MockBean
    private ListContentProviderUseCase listContentProviderUseCase;

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() throws Exception {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var requestBody = new CreateSubProviderRequest(
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

        when(createContentProviderUseCase.execute(any()))
                .thenReturn(CreateContentProviderOutput.from("123"));

        final var request = post("/contentproviders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/contentproviders/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createContentProviderUseCase, times(1)).execute(argThat(provider ->
                Objects.equals(expectedType, provider.type())
                        && Objects.equals(expectedName, provider.name())
                        && Objects.equals(expectedBaseUrl, provider.baseUrl())
                        && Objects.equals(expectedAuthenticationType, provider.authenticationType())
                        && Objects.equals(expectedClientId, provider.clientId())
                        && Objects.equals(expectedClientSecret, provider.clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, provider.authorizationUrl())
                        && Objects.equals(expectedTokenUrl, provider.tokenUrl())
        ));
    }

    @Test
    void givenAInvalidName_whenCallsCreate_thenShouldReturnNotification() throws Exception {
        final var expectedType = "Google";
        final String expectedName = null;
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedMessage = "'name' should not be null";

        final var requestBody = new CreateSubProviderRequest(
                expectedType,
                null,
                expectedBaseUrl,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        when(createContentProviderUseCase.execute(any()))
                .thenThrow(DomainException.with(expectedMessage));

        final var request = post("/contentproviders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createContentProviderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedType, cmd.type())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBaseUrl, cmd.baseUrl())
                        && Objects.equals(expectedClientId, cmd.clientId())
                        && Objects.equals(expectedClientSecret, cmd.clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, cmd.authorizationUrl())
                        && Objects.equals(expectedTokenUrl, cmd.tokenUrl())
        ));
    }

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() throws Exception {
        final var expectedType = "Google Drive";
        final var expectedName = "Google Integration";
        final var expectedBaseUrl = "http://www.google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = true;

        final var provider = ContentProvider.create(
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

        final var expectedId = provider.getId().getValue();

        when(getContentProviderUseCase.execute(any()))
                .thenReturn(ContentProviderOutput.from(provider));

        final var request = get("/contentproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.type", equalTo(expectedType)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.base_url", equalTo(expectedBaseUrl)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.last_sync", equalTo(null)))
                .andExpect(jsonPath("$.client_id", equalTo(expectedClientId)))
                .andExpect(jsonPath("$.client_secret", equalTo(expectedClientSecret)))
                .andExpect(jsonPath("$.authorization_url", equalTo(expectedAuthorizationUrl)))
                .andExpect(jsonPath("$.token_url", equalTo(expectedTokenUrl)));

        verify(getContentProviderUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAInvalidId_whenCallsGet_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "SubProvider with ID 123 was not found";
        final var expectedId = SubProviderID.from("123");

        when(getContentProviderUseCase.execute(any()))
                .thenThrow(NotFoundException.with(SubProvider.class, expectedId));

        final var request = get("/contentproviders/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidCommand_whenCallsUpdate_shouldReturnId() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        when(updateContentProviderUseCase.execute(any()))
                .thenReturn(UpdateContentProviderOutput.from(expectedId));

        final var aCommand = new UpdateSubProviderRequest(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var request = put("/contentproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateContentProviderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBaseUrl, cmd.baseUrl())
                        && Objects.equals(expectedIsActive, cmd.active())
                        && Objects.equals(expectedAuthenticationType, cmd.authenticationType())
                        && Objects.equals(expectedClientId, cmd.clientId())
                        && Objects.equals(expectedClientSecret, cmd.clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, cmd.authorizationUrl())
                        && Objects.equals(expectedTokenUrl, cmd.tokenUrl())
        ));
    }

    @Test
    void givenAInvalidName_whenCallsUpdate_thenShouldReturnDomainException() throws Exception {
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";

        when(updateContentProviderUseCase.execute(any()))
                .thenThrow(DomainException.with(expectedMessage));

        final var aCommand = new UpdateSubProviderRequest(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var request = put("/contentproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateContentProviderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBaseUrl, cmd.baseUrl())
                        && Objects.equals(expectedIsActive, cmd.active())
                        && Objects.equals(expectedAuthenticationType, cmd.authenticationType())
                        && Objects.equals(expectedClientId, cmd.clientId())
                        && Objects.equals(expectedClientSecret, cmd.clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, cmd.authorizationUrl())
                        && Objects.equals(expectedTokenUrl, cmd.tokenUrl())
        ));
    }

    @Test
    void givenACommandWithInvalidID_whenCallsUpdate_shouldReturnNotFoundException() throws Exception {
        final var expectedId = "not-found";
        final var expectedName = "Patreon";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var expectedErrorMessage = "SubProvider with ID not-found was not found";

        when(updateContentProviderUseCase.execute(any()))
                .thenThrow(NotFoundException.with(SubProvider.class, SubProviderID.from(expectedId)));

        final var aCommand = new UpdateSubProviderRequest(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var request = put("/contentproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateContentProviderUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedBaseUrl, cmd.baseUrl())
                        && Objects.equals(expectedIsActive, cmd.active())
                        && Objects.equals(expectedAuthenticationType, cmd.authenticationType())
                        && Objects.equals(expectedClientId, cmd.clientId())
                        && Objects.equals(expectedClientSecret, cmd.clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, cmd.authorizationUrl())
                        && Objects.equals(expectedTokenUrl, cmd.tokenUrl())
        ));
    }

    @Test
    void givenAValidId_whenCallsDelete_shouldReturnNoContent() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteContentProviderUseCase).execute(any());

        final var request = delete("/contentproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteContentProviderUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenValidParams_whenCallsList_shouldReturnProviders() throws Exception {
        final var provider = ContentProvider.create(
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

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ContentProviderListOutput.from(provider));

        when(listContentProviderUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/contentproviders")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(provider.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(provider.getName())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(provider.isActive())))
                .andExpect(jsonPath("$.items[0].last_sync", equalTo(provider.getLastSync())));

        verify(listContentProviderUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
