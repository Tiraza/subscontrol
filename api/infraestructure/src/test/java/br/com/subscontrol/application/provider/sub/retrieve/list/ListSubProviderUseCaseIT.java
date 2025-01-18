package br.com.subscontrol.application.provider.sub.retrieve.list;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class ListSubProviderUseCaseIT {

    @Autowired
    private ListSubProviderUseCase useCase;

    @Autowired
    private SubProviderGateway gateway;

    @Autowired
    private SubProviderRepository repository;

    @Test
    void givenAValidQuery_whenCallsList_shouldReturnProviders() {
        final var providers = List.of(
                createProviderForTest("Patreon"),
                createProviderForTest("Kickstarter")
        );

        assertEquals(0, repository.count());

        repository.saveAllAndFlush(providers.stream().map(SubProviderJpaEntity::from).toList());

        assertEquals(2, repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = providers.stream().map(SubProviderListOutput::from).toList();

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

        final var expectedItems = List.<SubProviderListOutput>of();

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
    }

    private static SubProvider createProviderForTest(final String name) {
        return SubProvider.create(
                SubProviderType.PATREON.getName(),
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
