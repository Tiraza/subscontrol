package br.com.subscontrol.application.tier.retrieve.list;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ListTierUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListTierUseCase useCase;

    @Mock
    private TierGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidQuery_whenCallsList_shouldReturnProviders() {
        final var tiers = List.of(
                createTier("Full Access", "Access all contents", "R$ 10,00"),
                createTier("Minor Access", "Access minor contents", "R$ 5,00")
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "a";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = tiers.stream().map(TierListOutput::from).toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                tiers
        );

        when(gateway.findAll(any())).thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(gateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var subs = List.<Tier>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "a";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<TierListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                subs
        );

        when(gateway.findAll(any())).thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = useCase.execute(aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(gateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListAndGatewayThrowsRandomError_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "a";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(gateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(gateway, times(1)).findAll(eq(aQuery));
    }

    private Tier createTier(String title, String description, String amount) {
        return  Tier.create(
                SubProviderID.unique().getValue(),
                UUID.randomUUID().toString(),
                title,
                description,
                amount
        );
    }

}
