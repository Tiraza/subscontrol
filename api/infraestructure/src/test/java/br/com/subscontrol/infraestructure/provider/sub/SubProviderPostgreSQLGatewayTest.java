package br.com.subscontrol.infraestructure.provider.sub;

import br.com.subscontrol.PostgreSQLGatewayTest;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PostgreSQLGatewayTest
class SubProviderPostgreSQLGatewayTest {

    @Autowired
    private SubProviderPostgreSQLGateway gateway;

    @Autowired
    private SubProviderRepository repository;

    @Test
    void givenAValidProvider_whenCallsCreate_shouldReturnNewProvider() {
        final var expectedType = SubProviderType.PATREON.getName();
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "https://www.patreon.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://patreon.com/authorization";
        final var expectedTokenUrl = "http://patreon.com/token";

        final var provider = SubProvider.create(
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
        assertEquals(expectedClientId, persistedProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, persistedProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, persistedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, persistedProvider.getAuthentication().getTokenUrl());
    }

    @Test
    void givenAValidProvider_whenCallsUpdate_shouldReturnProviderUpdate() {
        final var expectedType = SubProviderType.PATREON.getName();
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "https://www.patreon.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://patreon.com/authorization";
        final var expectedTokenUrl = "http://patreon.com/token";
        final var expectedIsActive = false;

        final var provider = SubProvider.create(
                expectedType,
                "Patreon",
                "https://www.patreon.com.br",
                AuthenticationType.CLIENT_SECRET.name(),
                "123",
                "123",
                "https://www.patreon.com.br",
                "https://www.patreon.com.br",
                null
        );

        assertEquals(0, repository.count());

        repository.saveAndFlush(SubProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        final var firstPersistedProvider = repository.findById(provider.getId().getValue()).get();

        assertEquals(provider.getId().getValue(), firstPersistedProvider.getId());
        assertEquals(provider.isActive(), firstPersistedProvider.isActive());
        assertEquals(provider.getLastSync(), firstPersistedProvider.getLastSync());
        assertNull(firstPersistedProvider.getLastSync());
        assertEquals(expectedType, firstPersistedProvider.getType().getName());
        assertEquals("Patreon", firstPersistedProvider.getName());
        assertEquals("https://www.patreon.com.br", firstPersistedProvider.getBaseUrl());
        assertEquals("123", firstPersistedProvider.getAuthentication().getClientId());
        assertEquals("123", firstPersistedProvider.getAuthentication().getClientSecret());
        assertEquals("https://www.patreon.com.br", firstPersistedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals("https://www.patreon.com.br", firstPersistedProvider.getAuthentication().getTokenUrl());

        final var updatedProvider = SubProvider.with(provider);
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
        assertEquals(expectedClientId, secondPersistedProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, secondPersistedProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, secondPersistedProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, secondPersistedProvider.getAuthentication().getTokenUrl());
    }

    @Test
    public void givenAPrePersistedProviderAndValidCategoryId_whenTryToDeleteIt_shouldDeleteProvider() {
        final var provider = SubProvider.create(
                SubProviderType.PATREON.getName(),
                "Patreon",
                "https://www.patreon.com.br",
                AuthenticationType.CLIENT_SECRET.name(),
                "123",
                "123",
                "https://www.patreon.com.br",
                "https://www.patreon.com.br",
                null
        );

        assertEquals(0, repository.count());

        repository.saveAndFlush(SubProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        gateway.deleteById(provider.getId());

        assertEquals(0, repository.count());
    }

    @Test
    public void givenInvalidProviderId_whenTryToDeleteIt_shouldBeOK() {
        assertEquals(0, repository.count());

        gateway.deleteById(SubProviderID.from("invalid"));

        assertEquals(0, repository.count());
    }

    @Test
    public void givenAPrePersistedProviderAndValidProviderId_whenCallsFindById_shouldReturnProvider() {
        final var expectedType = SubProviderType.PATREON.getName();
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "https://www.patreon.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://patreon.com/authorization";
        final var expectedTokenUrl = "http://patreon.com/token";

        final var provider = SubProvider.create(
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

        repository.saveAndFlush(SubProviderJpaEntity.from(provider));

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
        assertEquals(expectedClientId, actualProvider.getAuthentication().getClientId());
        assertEquals(expectedClientSecret, actualProvider.getAuthentication().getClientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.getAuthentication().getAuthorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.getAuthentication().getTokenUrl());
    }

    @Test
    public void givenValidProviderIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, repository.count());

        final var actualProvider = gateway.findById(SubProviderID.from("empty"));

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
            "ko,0,10,1,1,Ko-fi",
            "tre,0,10,1,1,Patreon",
            "icks,0,10,1,1,Kickstarter",
            "pod,0,10,1,1,Podia",
            "ego,0,10,1,1,Indiegogo"
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
            "name,asc,0,10,5,5,Indiegogo",
            "name,desc,0,10,5,5,Podia",
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
            "0,2,2,5,Indiegogo;Kickstarter",
            "1,2,2,5,Ko-fi;Patreon",
            "2,2,1,5,Podia"
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
                        createProvider("Patreon"),
                        createProvider("Kickstarter"),
                        createProvider("Ko-fi"),
                        createProvider("Podia"),
                        createProvider("Indiegogo")
                )
        );
    }

    private SubProviderJpaEntity createProvider(final String name) {
        return SubProviderJpaEntity.from(
                SubProvider.create(
                        SubProviderType.PATREON.getName(),
                        name,
                        "https://www.patreon.com.br",
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
