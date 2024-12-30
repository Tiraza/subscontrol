package br.com.subscontrol.application.sub.retrieve.list;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.sub.Sub;
import br.com.subscontrol.domain.sub.SubGateway;
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

public class ListSubUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListSubUseCase useCase;

    @Mock
    private SubGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidQuery_whenCallsList_shouldReturnProviders() {
        final var subs = List.of(
                Sub.create(UUID.randomUUID().toString(), "Mario", "mario@mail.com"),
                Sub.create(UUID.randomUUID().toString(), "Luigi", "luigi@mail.com")
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "i";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = subs.stream().map(SubListOutput::from).toList();

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
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var subs = List.<Sub>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "i";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<SubListOutput>of();

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
        final var expectedTerms = "i";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(gateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualOutput = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(gateway, times(1)).findAll(eq(aQuery));
    }
}