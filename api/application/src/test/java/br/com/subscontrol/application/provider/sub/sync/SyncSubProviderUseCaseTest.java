package br.com.subscontrol.application.provider.sub.sync;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.domain.tier.Tier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class SyncSubProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultSyncSubProviderUseCase useCase;

    @Mock
    private SubProviderGateway gateway;

    @Mock
    private Map<SubProviderType, SubSynchronizer> synchronizers;

    @Spy
    private SubSynchronizer synchronizer;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway, synchronizer, synchronizers);
    }

    @Test
    void givenAValidParameters_whenCallsExecuteAndDoesNotExistsProvider_shouldReturnNotFound() {
        final var expectedErrorMessage = "SubProvider with ID 123 was not found";
        final var expectedId = SubProviderID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidParameters_whenCallsExecuteAndDoesNotExistsSynchronizer_shouldReturnIllegalArgument() {
        final var expectedType = SubProviderType.PATREON;
        final var expectedErrorMessage = "Unknown SubProviderType: PATREON";

        final var provider = SubProvider.create(
                expectedType.getName(),
                "Patreon Integration",
                "https://www.patreon.com",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token");

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));
        when(synchronizers.get(expectedType)).thenReturn(null);

        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
        Mockito.verify(synchronizers, times(1)).get(eq(expectedType));
    }

    @Test
    void givenAValidParametersWithoutAuthentication_whenCallsExecute_shouldBeOk() {
        final var expectedType = SubProviderType.PATREON;

        final var provider = SubProvider.create(
                expectedType,
                "Patreon Integration",
                "https://www.patreon.com",
                null);

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));
        when(synchronizers.get(expectedType)).thenReturn(synchronizer);

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
        Mockito.verify(synchronizers, times(1)).get(eq(expectedType));
        Mockito.verify(synchronizer, times(0)).synchronizeTiers(any());
        Mockito.verify(synchronizer, times(0)).synchronizeSubsFromTier(any(), any());
    }

    @Test
    void givenAValidParameters_whenCallsExecuteAndDoNotSyncAnyTier_shouldBeOk() {
        final var expectedType = SubProviderType.PATREON;

        final var expetedAuthentication = new Authentication(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token"
        );

        final var provider = SubProvider.create(
                expectedType,
                "Patreon Integration",
                "https://www.patreon.com",
                expetedAuthentication);

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));
        when(synchronizers.get(expectedType)).thenReturn(synchronizer);
        when(synchronizer.synchronizeTiers(expetedAuthentication)).thenReturn(List.of());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
        Mockito.verify(synchronizers, times(1)).get(eq(expectedType));
        Mockito.verify(synchronizer, times(1)).synchronizeTiers(expetedAuthentication);
        Mockito.verify(synchronizer, times(0)).synchronizeSubsFromTier(any(), any());
    }

    @Test
    void givenAValidParameters_whenCallsExecuteAndSyncAnyTier_subsShouldBySync() {
        final var expectedType = SubProviderType.PATREON;

        final var expetedAuthentication = new Authentication(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token"
        );

        final var provider = SubProvider.create(
                expectedType,
                "Patreon Integration",
                "https://www.patreon.com",
                expetedAuthentication);

        final var expectedId = provider.getId();

        final var tier = Tier.create(
                expectedId.getValue(),
                UUID.randomUUID().toString(),
                "Subscriber",
                "Default Subscriber",
                "1500"
        );

        when(gateway.findById(any())).thenReturn(Optional.of(provider));
        when(synchronizers.get(expectedType)).thenReturn(synchronizer);
        when(synchronizer.synchronizeTiers(expetedAuthentication)).thenReturn(List.of(tier));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
        Mockito.verify(synchronizers, times(1)).get(eq(expectedType));
        Mockito.verify(synchronizer, times(1)).synchronizeTiers(expetedAuthentication);
        Mockito.verify(synchronizer, times(1)).synchronizeSubsFromTier(expetedAuthentication, tier);
    }

}
