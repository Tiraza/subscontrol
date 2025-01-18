package br.com.subscontrol.infraestructure.provider.content;

import br.com.subscontrol.PostgreSQLGatewayTest;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PostgreSQLGatewayTest
public class ContentProviderPostgreSQLGatewayTest {

    @Autowired
    private ContentProviderPostgreSQLGateway gateway;

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAValidProvider_whenCallsCreate_shouldReturnNewProvider() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE.getName();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var provider = ContentProvider.create(
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

        assertEquals(0, repository.count());

        final var returnedProvider = gateway.create(provider);

        assertEquals(1, repository.count());

        assertEquals(provider.getId(), returnedProvider.getId());
        assertEquals(provider.isActive(), returnedProvider.isActive());
        assertTrue(returnedProvider.isActive());
        assertEquals(provider.getLastSync(), returnedProvider.getLastSync());
        assertNull(returnedProvider.getLastSync());
        assertEquals(expectedType, returnedProvider.getType().getName());
        assertEquals(expectedName, returnedProvider.getName());
        assertEquals(expectedBaseUrl, returnedProvider.getBaseUrl());
        assertEquals(expectedAuthenticationType, returnedProvider.getAuthentication().getType());
        assertEquals(expectedClientId, returnedProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, returnedProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, returnedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, returnedProvider.getAuthentication().getTokenUrl());

        final var persistedProvider = repository.findById(provider.getId().getValue()).get();

        assertEquals(provider.getId().getValue(), persistedProvider.getId());
        assertEquals(provider.isActive(), persistedProvider.isActive());
        assertTrue(persistedProvider.isActive());
        assertEquals(provider.getLastSync(), persistedProvider.getLastSync());
        assertNull(persistedProvider.getLastSync());
        assertEquals(expectedType, persistedProvider.getType().getName());
        assertEquals(expectedName, persistedProvider.getName());
        assertEquals(expectedBaseUrl, persistedProvider.getBaseUrl());
        assertEquals(expectedAuthenticationType, persistedProvider.getAuthentication().getType());
        assertEquals(expectedClientId, persistedProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, persistedProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, persistedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, persistedProvider.getAuthentication().getTokenUrl());
    }

    @Test
    void givenAValidProvider_whenCallsUpdate_shouldReturnProviderUpdate() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE.getName();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = false;

        final var provider = ContentProvider.create(
                expectedType,
                "Google",
                "https://www.google.com.br",
                expectedAuthenticationType.name(),
                "123",
                "123",
                "https://www.google.com.br",
                "https://www.google.com.br",
                null
        );

        assertEquals(0, repository.count());

        repository.saveAndFlush(ContentProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        final var firstPersistedProvider = repository.findById(provider.getId().getValue()).get();

        assertEquals(provider.getId().getValue(), firstPersistedProvider.getId());
        assertEquals(provider.isActive(), firstPersistedProvider.isActive());
        assertEquals(provider.getLastSync(), firstPersistedProvider.getLastSync());
        assertNull(firstPersistedProvider.getLastSync());
        assertEquals(expectedType, firstPersistedProvider.getType().getName());
        assertEquals("Google", firstPersistedProvider.getName());
        assertEquals("https://www.google.com.br", firstPersistedProvider.getBaseUrl());
        assertEquals("123", firstPersistedProvider.getAuthentication().getClientId());
        assertEquals("123", firstPersistedProvider.getAuthentication().getClientSecret());
        assertEquals("https://www.google.com.br", firstPersistedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals("https://www.google.com.br", firstPersistedProvider.getAuthentication().getTokenUrl());

        final var updatedProvider = ContentProvider.with(provider);
        updatedProvider.update(expectedName, expectedBaseUrl, expectedIsActive);
        updatedProvider.updateAuthentication(
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var returnedProvider = gateway.update(updatedProvider);

        assertEquals(1, repository.count());

        assertEquals(updatedProvider.getId(), returnedProvider.getId());
        assertEquals(updatedProvider.isActive(), returnedProvider.isActive());
        assertEquals(updatedProvider.getLastSync(), returnedProvider.getLastSync());
        assertNull(returnedProvider.getLastSync());
        assertEquals(expectedType, returnedProvider.getType().getName());
        assertEquals(expectedName, returnedProvider.getName());
        assertEquals(expectedBaseUrl, returnedProvider.getBaseUrl());
        assertEquals(expectedAuthenticationType, returnedProvider.getAuthentication().getType());
        assertEquals(expectedClientId, returnedProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, returnedProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, returnedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, returnedProvider.getAuthentication().getTokenUrl());

        final var secondPersistedProvider = repository.findById(updatedProvider.getId().getValue()).get();

        assertEquals(updatedProvider.getId().getValue(), secondPersistedProvider.getId());
        assertEquals(updatedProvider.isActive(), secondPersistedProvider.isActive());
        assertEquals(updatedProvider.getLastSync(), secondPersistedProvider.getLastSync());
        assertNull(secondPersistedProvider.getLastSync());
        assertEquals(expectedType, secondPersistedProvider.getType().getName());
        assertEquals(expectedName, secondPersistedProvider.getName());
        assertEquals(expectedBaseUrl, secondPersistedProvider.getBaseUrl());
        assertEquals(expectedAuthenticationType, secondPersistedProvider.getAuthentication().getType());
        assertEquals(expectedClientId, secondPersistedProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, secondPersistedProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, secondPersistedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, secondPersistedProvider.getAuthentication().getTokenUrl());
    }

    @Test
    public void givenAPrePersistedProviderAndValidCategoryId_whenTryToDeleteIt_shouldDeleteProvider() {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Drive",
                "https://www.google.com.br",
                AuthenticationType.CLIENT_SECRET.name(),
                "123",
                "123",
                "https://www.google.com.br",
                "https://www.google.com.br",
                null
        );

        assertEquals(0, repository.count());

        repository.saveAndFlush(ContentProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        gateway.deleteById(provider.getId());

        assertEquals(0, repository.count());
    }

    @Test
    public void givenInvalidProviderId_whenTryToDeleteIt_shouldBeOK() {
        assertEquals(0, repository.count());

        gateway.deleteById(ContentProviderID.from("invalid"));

        assertEquals(0, repository.count());
    }

    @Test
    public void givenAPrePersistedProviderAndValidProviderId_whenCallsFindById_shouldReturnProvider() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE.getName();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var provider = ContentProvider.create(
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

        assertEquals(0, repository.count());

        repository.saveAndFlush(ContentProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        final var actualProvider = gateway.findById(provider.getId()).get();

        assertEquals(1, repository.count());

        assertEquals(provider.getId(), actualProvider.getId());
        assertEquals(provider.isActive(), actualProvider.isActive());
        assertTrue(actualProvider.isActive());
        assertEquals(provider.getLastSync(), actualProvider.getLastSync());
        assertNull(actualProvider.getLastSync());
        assertEquals(expectedType, actualProvider.getType().getName());
        assertEquals(expectedName, actualProvider.getName());
        assertEquals(expectedBaseUrl, actualProvider.getBaseUrl());
        assertEquals(expectedAuthenticationType, actualProvider.getAuthentication().getType());
        assertEquals(expectedClientId, actualProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, actualProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.getAuthentication().getTokenUrl());
    }

    @Test
    public void givenValidProviderIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, repository.count());

        final var actualProvider = gateway.findById(ContentProviderID.from("empty"));

        assertTrue(actualProvider.isEmpty());
    }

    @Test
    public void givenEmptyProviderTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        assertEquals(0, repository.count());

        final var actualResult = gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "ogle,0,10,1,1,Google Drive",
            "box,0,10,1,1,Dropbox",
            "ecl,0,10,1,1,FileCloud",
            "one,0,10,1,1,OneDrive",
            "ref,0,10,1,1,ShareFile"
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedProviderName
    ) {
        mockProviders();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedProviderName, actualResult.items().getFirst().getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Dropbox",
            "name,desc,0,10,5,5,ShareFile",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedProviderName
    ) {
        mockProviders();

        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedProviderName, actualResult.items().getFirst().getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Dropbox;FileCloud",
            "1,2,2,5,Google Drive;OneDrive",
            "2,2,1,5,ShareFile"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedProviders
    ) {
        mockProviders();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());

        int index = 0;
        for (String expectedName : expectedProviders.split(";")) {
            final var actualName = actualResult.items().get(index).getName();
            assertEquals(expectedName, actualName);
            index++;
        }
    }

    private void mockProviders() {
        repository.saveAllAndFlush(
                List.of(
                        createProvider("Google Drive"),
                        createProvider("Dropbox"),
                        createProvider("OneDrive"),
                        createProvider("FileCloud"),
                        createProvider("ShareFile")
                )
        );
    }

    private ContentProviderJpaEntity createProvider(final String name) {
        return ContentProviderJpaEntity.from(
                ContentProvider.create(
                        ContentProviderType.GOOGLE_DRIVE.getName(),
                        name,
                        "https://www.google.com",
                        AuthenticationType.CLIENT_SECRET.name(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "/authorization",
                        "/token",
                        null
                )
        );
    }

}
