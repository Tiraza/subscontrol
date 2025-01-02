package br.com.subscontrol.infraestructure.provider.content;

import br.com.subscontrol.PostgreSQLGatewayTest;
import br.com.subscontrol.domain.pagination.SearchQuery;
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
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var provider = ContentProvider.create(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl);

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
        assertEquals(expectedClientId, returnedProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, returnedProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, returnedProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, returnedProvider.getAuthentication().tokenUrl());

        final var persistedProvider = repository.findById(provider.getId().getValue()).get();

        assertEquals(provider.getId().getValue(), persistedProvider.getId());
        assertEquals(provider.isActive(), persistedProvider.isActive());
        assertTrue(persistedProvider.isActive());
        assertEquals(provider.getLastSync(), persistedProvider.getLastSync());
        assertNull(persistedProvider.getLastSync());
        assertEquals(expectedType, persistedProvider.getType().getName());
        assertEquals(expectedName, persistedProvider.getName());
        assertEquals(expectedBaseUrl, persistedProvider.getBaseUrl());
        assertEquals(expectedClientId, persistedProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, persistedProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, persistedProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, persistedProvider.getAuthentication().tokenUrl());
    }

    @Test
    void givenAValidProvider_whenCallsUpdate_shouldReturnProviderUpdate() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE.getName();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";
        final var expectedIsActive = false;

        final var provider = ContentProvider.create(
                expectedType,
                "Google",
                "https://www.google.com.br",
                "123",
                "123",
                "https://www.google.com.br",
                "https://www.google.com.br");

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
        assertEquals("123", firstPersistedProvider.getAuthentication().clientId());
        assertEquals("123", firstPersistedProvider.getAuthentication().clientSecret());
        assertEquals("https://www.google.com.br", firstPersistedProvider.getAuthentication().authorizationUrl());
        assertEquals("https://www.google.com.br", firstPersistedProvider.getAuthentication().tokenUrl());

        final var updatedProvider = ContentProvider.with(provider);
        updatedProvider.update(
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
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
        assertEquals(expectedClientId, returnedProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, returnedProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, returnedProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, returnedProvider.getAuthentication().tokenUrl());

        final var secondPersistedProvider = repository.findById(updatedProvider.getId().getValue()).get();

        assertEquals(updatedProvider.getId().getValue(), secondPersistedProvider.getId());
        assertEquals(updatedProvider.isActive(), secondPersistedProvider.isActive());
        assertEquals(updatedProvider.getLastSync(), secondPersistedProvider.getLastSync());
        assertNull(secondPersistedProvider.getLastSync());
        assertEquals(expectedType, secondPersistedProvider.getType().getName());
        assertEquals(expectedName, secondPersistedProvider.getName());
        assertEquals(expectedBaseUrl, secondPersistedProvider.getBaseUrl());
        assertEquals(expectedClientId, secondPersistedProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, secondPersistedProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, secondPersistedProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, secondPersistedProvider.getAuthentication().tokenUrl());
    }

    @Test
    public void givenAPrePersistedProviderAndValidCategoryId_whenTryToDeleteIt_shouldDeleteProvider() {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Drive",
                "https://www.google.com.br",
                "123",
                "123",
                "https://www.google.com.br",
                "https://www.google.com.br");

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
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var provider = ContentProvider.create(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl);

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
        assertEquals(expectedClientId, actualProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, actualProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.getAuthentication().tokenUrl());
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
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "/authorization",
                        "/token"
                )
        );
    }

}
