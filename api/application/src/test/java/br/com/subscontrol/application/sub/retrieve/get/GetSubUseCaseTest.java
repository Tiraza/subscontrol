package br.com.subscontrol.application.sub.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.sub.Sub;
import br.com.subscontrol.domain.sub.SubGateway;
import br.com.subscontrol.domain.sub.SubID;
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

public class GetSubUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetSubUseCase useCase;

    @Mock
    private SubGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() {
        final var expectedProvidedId = UUID.randomUUID().toString();
        final var expectedName = "Sub";
        final var expectedEmail = "sub@mail.com";

        final var sub = Sub.create(expectedProvidedId, expectedName, expectedEmail);
        final var expectedId = sub.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(sub));

        final var actualSub = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualSub.id());
        assertEquals(expectedName, actualSub.name());
        assertEquals(expectedProvidedId, actualSub.providedId());
        assertEquals(expectedEmail, actualSub.email());
        assertTrue(actualSub.isActive());
        assertNotNull(actualSub.createdAt());
        assertNotNull(actualSub.updatedAt());
        assertNull(actualSub.deletedAt());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Sub with ID 123 was not found";
        final var expectedId = SubID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }
}
