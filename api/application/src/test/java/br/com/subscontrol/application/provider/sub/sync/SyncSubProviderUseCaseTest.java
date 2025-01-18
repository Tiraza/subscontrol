package br.com.subscontrol.application.provider.sub.sync;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

        verify(gateway, times(1)).findById(eq(expectedId));
    }

    @ParameterizedTest
    @MethodSource("authenticationTypes")
    void givenAValidParameters_whenCallsExecuteAndDoesNotExistsSynchronizer_shouldReturnIllegalArgument(
            final Authentication authentication
    ) {
        final var expectedType = SubProviderType.PATREON;
        final var expectedErrorMessage = "Unknown SubProviderType: PATREON";

        final var provider = SubProvider.with(
                authentication.getProviderID().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "https://www.patreon.com",
                true,
                null,
                authentication
        );

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));
        when(synchronizers.get(expectedType)).thenReturn(null);

        final var actualException = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(synchronizers, times(1)).get(eq(expectedType));
        verify(synchronizer, times(0)).synchronizeTiers(any());
        verify(synchronizer, times(0)).synchronizeSubsFromTier(any(), any());
    }

    @ParameterizedTest
    @MethodSource("authenticationTypes")
    void givenAValidParameters_whenCallsExecuteAndDoNotSyncAnyTier_shouldBeOk(
            final Authentication expectedAuthentication
    ) {
        final var expectedType = SubProviderType.PATREON;

        final var provider = SubProvider.with(
                expectedAuthentication.getProviderID().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "https://www.patreon.com",
                true,
                null,
                expectedAuthentication
        );

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));
        when(synchronizers.get(expectedType)).thenReturn(synchronizer);
        when(synchronizer.synchronizeTiers(expectedAuthentication)).thenReturn(List.of());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(synchronizers, times(1)).get(eq(expectedType));
        verify(synchronizer, times(1)).synchronizeTiers(expectedAuthentication);
        verify(synchronizer, times(0)).synchronizeSubsFromTier(any(), any());
    }

    @ParameterizedTest
    @MethodSource("authenticationTypes")
    void givenAValidParameters_whenCallsExecuteAndSyncAnyTier_subsShouldBeSync(
            final Authentication expectedAuthentication
    ) {
        final var expectedType = SubProviderType.PATREON;

        final var provider = SubProvider.with(
                expectedAuthentication.getProviderID().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "https://www.patreon.com",
                true,
                null,
                expectedAuthentication
        );

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
        when(synchronizer.synchronizeTiers(expectedAuthentication)).thenReturn(List.of(tier));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(synchronizers, times(1)).get(eq(expectedType));
        verify(synchronizer, times(1)).synchronizeTiers(expectedAuthentication);
        verify(synchronizer, times(1)).synchronizeSubsFromTier(expectedAuthentication, tier);
    }

    @ParameterizedTest
    @MethodSource("authenticationTypes")
    void givenAValidInactiveProvider_whenCallsExecute_subsShouldBeNotSync(
            final Authentication expectedAuthentication
    ) {
        final var expectedType = SubProviderType.PATREON;

        final var provider = SubProvider.with(
                expectedAuthentication.getProviderID().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "https://www.patreon.com",
                false,
                null,
                expectedAuthentication
        );

        final var expectedId = provider.getId();

        final var tier = Tier.create(
                expectedId.getValue(),
                UUID.randomUUID().toString(),
                "Subscriber",
                "Default Subscriber",
                "1500"
        );

        when(gateway.findById(any())).thenReturn(Optional.of(provider));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(synchronizers, times(0)).get(eq(expectedType));
        verify(synchronizer, times(0)).synchronizeTiers(expectedAuthentication);
        verify(synchronizer, times(0)).synchronizeSubsFromTier(expectedAuthentication, tier);
    }

    @ParameterizedTest
    @MethodSource("authenticationTypes")
    void givenAValidParams_whenCallsExecute_subsShouldBeSync(
            final Authentication expectedAuthentication
    ) {
        final var expectedType = SubProviderType.PATREON;
        final var lastSync = InstantUtils.now();

        final var provider = SubProvider.with(
                expectedAuthentication.getProviderID().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "https://www.patreon.com",
                true,
                lastSync,
                expectedAuthentication
        );

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
        when(synchronizer.synchronizeTiers(expectedAuthentication)).thenReturn(List.of(tier));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).findById(eq(expectedId));
        verify(gateway, times(1)).update(eq(provider));
        verify(synchronizers, times(1)).get(eq(expectedType));
        verify(synchronizer, times(1)).synchronizeTiers(expectedAuthentication);
        verify(synchronizer, times(1)).synchronizeSubsFromTier(expectedAuthentication, tier);
    }

    private static Stream<Arguments> authenticationTypes() {
        return Stream.of(
                Arguments.of(Authentication.withClientSecret(
                        SubProviderID.unique(),
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "/authorization",
                        "/token")
                ),
                Arguments.of(Authentication.withFile(
                        SubProviderID.unique(),
                        new byte[1])
                )
        );
    }

}
