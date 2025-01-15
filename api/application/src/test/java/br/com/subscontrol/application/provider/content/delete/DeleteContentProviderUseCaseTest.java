package br.com.subscontrol.application.provider.content.delete;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
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

class DeleteContentProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteContentProviderUseCase useCase;

    @Mock
    private ContentProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsDelete_shouldDelete() {
        final var contentProvider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Drive Integration",
                "http://google.com",
                AuthenticationType.CLIENT_SECRET.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );

        final var expectedId = contentProvider.getId();

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var expectedId = ContentProviderID.from("123");

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var contentProvider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Drive Integration",
                "http://google.com",
                AuthenticationType.CLIENT_SECRET.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );

        final var expectedId = contentProvider.getId();

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }
}
