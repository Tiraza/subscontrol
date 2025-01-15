package br.com.subscontrol.application.provider.sub.delete;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteSubProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteSubProviderUseCase useCase;

    @Mock
    private SubProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsDelete_shouldDelete() {
        final var subProvider = SubProvider.create(
                SubProviderType.PATREON.getName(),
                "Patreon Integration",
                "http://patreon.com",
                AuthenticationType.CLIENT_SECRET.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );

        final var expectedId = subProvider.getId();

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var expectedId = SubProviderID.from("123");

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var subProvider = SubProvider.create(
                SubProviderType.PATREON.getName(),
                "Patreon Integration",
                "http://patreon.com",
                AuthenticationType.CLIENT_SECRET.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );

        final var expectedId = subProvider.getId();

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }
}
