package br.com.subscontrol.application.provider.content.retrieve.list;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class ListContentProviderUseCaseIT {

    @Autowired
    private ListContentProviderUseCase useCase;

    @SpyBean
    private ContentProviderGateway gateway;

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAValidQuery_whenCallsList_shouldReturnProviders() {
        final var providers = List.of(
                createProviderForTest("Google"),
                createProviderForTest("Drive")
        );

        assertEquals(0, repository.count());

        repository.saveAllAndFlush(providers.stream().map(ContentProviderJpaEntity::from).toList());

        assertEquals(2, repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = providers.stream().map(ContentProviderListOutput::from).toList();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertTrue(
                expectedItems.size() == actualOutput.items().size()
                        && expectedItems.containsAll(actualOutput.items())
        );
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<ContentProviderListOutput>of();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
    }

    private static ContentProvider createProviderForTest(final String name) {
        return ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                name,
                null,
                AuthenticationType.CLIENT_SECRET.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null);
    }

}
