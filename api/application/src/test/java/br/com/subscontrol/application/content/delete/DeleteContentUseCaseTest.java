package br.com.subscontrol.application.content.delete;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.content.ContentID;
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

public class DeleteContentUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteContentUseCase useCase;

    @Mock
    private ContentGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsDelete_shouldDelete() {
        final var content = Content.create(UUID.randomUUID().toString(), "Shared Folder");
        final var expectedId = content.getId();

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var expectedId = ContentID.from("123");

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var content = Content.create(UUID.randomUUID().toString(), "Shared Folder");
        final var expectedId = content.getId();

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }
}
