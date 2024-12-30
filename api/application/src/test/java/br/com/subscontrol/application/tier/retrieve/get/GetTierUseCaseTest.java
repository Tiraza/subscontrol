package br.com.subscontrol.application.tier.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierGateway;
import br.com.subscontrol.domain.tier.TierID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class GetTierUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetTierUseCase useCase;

    @Mock
    private TierGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() {
        final var subProviderID = SubProviderID.unique();
        final var expectedProvidedId = UUID.randomUUID().toString();
        final var expectedTitle = "Full Access";
        final var expectedDescription = "Access all contents";
        final var expectedAmount = "R$ 10,00";

        final var tier = Tier.create(
                subProviderID.getValue(),
                expectedProvidedId,
                expectedTitle,
                expectedDescription,
                expectedAmount
        );

        final var expectedId = tier.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(tier));

        final var actualTier = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualTier.id());
        assertEquals(subProviderID.getValue(), actualTier.subProviderID());
        assertEquals(expectedProvidedId, actualTier.providedId());
        assertEquals(expectedTitle, actualTier.title());
        assertEquals(expectedDescription, actualTier.description());
        assertEquals(expectedAmount, actualTier.amount());
        assertTrue(actualTier.isActive());
        assertNotNull(actualTier.createdAt());
        assertNotNull(actualTier.updatedAt());
        assertNull(actualTier.deletedAt());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Tier with ID 123 was not found";
        final var expectedId = TierID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }
}
