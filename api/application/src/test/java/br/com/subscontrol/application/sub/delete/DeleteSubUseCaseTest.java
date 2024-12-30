package br.com.subscontrol.application.sub.delete;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.sub.SubGateway;
import br.com.subscontrol.domain.sub.SubID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteSubUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteSubUseCase useCase;

    @Mock
    private SubGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsDelete_shouldDelete() {
        final var expectedId = SubID.unique();

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var expectedId = SubID.from("123");

        doNothing().when(gateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var expectedId = SubID.unique();

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(any());

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, times(1)).deleteById(expectedId);
    }

}
