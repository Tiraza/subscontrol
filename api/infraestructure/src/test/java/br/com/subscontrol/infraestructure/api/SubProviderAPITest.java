package br.com.subscontrol.infraestructure.api;

import br.com.subscontrol.ControllerTest;
import br.com.subscontrol.application.provider.sub.create.CreateSubProviderOutput;
import br.com.subscontrol.application.provider.sub.create.CreateSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.delete.DeleteSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.get.GetSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.get.SubProviderOutPut;
import br.com.subscontrol.application.provider.sub.retrieve.list.ListSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.list.SubProviderListOutput;
import br.com.subscontrol.application.provider.sub.update.UpdateSubProviderOutput;
import br.com.subscontrol.application.provider.sub.update.UpdateSubProviderUseCase;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
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

@ControllerTest(controllers = SubProviderAPI.class)
class SubProviderAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateSubProviderUseCase createSubProviderUseCase;

    @MockBean
    private GetSubProviderUseCase getSubProviderUseCase;

    @MockBean
    private UpdateSubProviderUseCase updateSubProviderUseCase;

    @MockBean
    private DeleteSubProviderUseCase deleteSubProviderUseCase;

    @MockBean
    private ListSubProviderUseCase listSubProviderUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
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

        when(createSubProviderUseCase.execute(any()))
                .thenReturn(CreateSubProviderOutput.from("123"));

        final var request = post("/subproviders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/subproviders/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createSubProviderUseCase, times(1)).execute(argThat(subProvider ->
                Objects.equals(expectedType, subProvider.type())
                        && Objects.equals(expectedName, subProvider.name())
                        && Objects.equals(expectedBaseUrl, subProvider.baseUrl())
                        && Objects.equals(expectedAuthenticationType, subProvider.authenticationType())
                        && Objects.equals(expectedClientId, subProvider.clientId())
                        && Objects.equals(expectedClientSecret, subProvider.clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, subProvider.authorizationUrl())
                        && Objects.equals(expectedTokenUrl, subProvider.tokenUrl())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final var expectedType = "Patreon";
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

        when(createSubProviderUseCase.execute(any()))
                .thenThrow(DomainException.with(expectedMessage));

        final var request = post("/subproviders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(requestBody));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createSubProviderUseCase, times(1)).execute(argThat(cmd ->
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
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = true;

        final var provider = SubProvider.create(
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

        when(getSubProviderUseCase.execute(any()))
                .thenReturn(SubProviderOutPut.from(provider));

        final var request = get("/subproviders/{id}", expectedId)
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

        verify(getSubProviderUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "SubProvider with ID 123 was not found";
        final var expectedId = SubProviderID.from("123");

        when(getSubProviderUseCase.execute(any()))
                .thenThrow(NotFoundException.with(SubProvider.class, expectedId));

        final var request = get("/subproviders/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        when(updateSubProviderUseCase.execute(any()))
                .thenReturn(UpdateSubProviderOutput.from(expectedId));

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

        final var request = put("/subproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateSubProviderUseCase, times(1)).execute(argThat(cmd ->
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
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
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

        when(updateSubProviderUseCase.execute(any()))
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

        final var request = put("/subproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateSubProviderUseCase, times(1)).execute(argThat(cmd ->
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
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception {
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

        when(updateSubProviderUseCase.execute(any()))
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

        final var request = put("/subproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateSubProviderUseCase, times(1)).execute(argThat(cmd ->
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
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteSubProviderUseCase).execute(any());

        final var request = delete("/subproviders/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteSubProviderUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        final var provider = SubProvider.create(
                "Patreon",
                "Patreon Integration",
                "http://www.patreon.com",
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

        final var expectedItems = List.of(SubProviderListOutput.from(provider));

        when(listSubProviderUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/subproviders")
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

        verify(listSubProviderUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }

}
