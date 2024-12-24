package br.com.subscontrol.application.content.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.exceptions.NotFoundException;
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

public class GetContentUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetContentUseCase useCase;

    @Mock
    private ContentGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() {
        final var expectedProvidedId = UUID.randomUUID().toString();
        final var expectedLabel = "Shared Folder";

        final var content = Content.create(expectedProvidedId, expectedLabel);
        final var expectedId = content.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(content));

        final var actualContent = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualContent.id());
        assertEquals(expectedProvidedId, actualContent.providedId());
        assertEquals(expectedLabel, actualContent.label());
        assertTrue(actualContent.isActive());
        assertNotNull(actualContent.createdAt());
        assertNotNull(actualContent.updatedAt());
        assertNull(actualContent.deletedAt());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Content with ID 123 was not found";
        final var expectedId = ContentID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }
}
