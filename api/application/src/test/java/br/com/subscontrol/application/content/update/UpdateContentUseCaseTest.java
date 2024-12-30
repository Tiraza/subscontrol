package br.com.subscontrol.application.content.update;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.content.Content;
import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateContentUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateContentUseCase useCase;

    @Mock
    private ContentGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdate_shouldReturnId() {
        final var content = Content.create(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Shared Folder");
        sleep();

        final var updatedAt = content.getUpdatedAt();
        final var expectedId = content.getId();
        final var expectedLabel = "Google Drive Shared Folder";
        final var expectedIsActive = false;

        final var command = UpdateContentCommand.with(
                expectedId.getValue(),
                expectedLabel,
                expectedIsActive
        );

        when(gateway.findById(any())).thenReturn(Optional.of(content));

        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(1)).update(argThat(updatedContent ->
                Objects.equals(expectedId, updatedContent.getId())
                        && Objects.equals(expectedLabel, updatedContent.getLabel())
                        && Objects.equals(expectedIsActive, updatedContent.isActive())
                        && updatedAt.isBefore(updatedContent.getUpdatedAt())
                        && !Objects.isNull(updatedContent.getDeletedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = "123";

        final var command = UpdateContentCommand.with(
                expectedId,
                "Shared Folder",
                true
        );

        when(gateway.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Content with ID 123 was not found", exception.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(ContentID.from(expectedId)));

        Mockito.verify(gateway, times(0)).update(any());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsUpdate_thenReceiveDomainException(String errorMessage, String label) {
        final var content = Content.create(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Shared Folder");

        final var expectedId = content.getId();
        final var expectedIsActive = true;

        final var command = UpdateContentCommand.with(
                expectedId.getValue(),
                label,
                expectedIsActive
        );

        when(gateway.findById(any())).thenReturn(Optional.of(content));

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(0)).update(any());
    }

    private static Stream<Arguments> provideArguments() {
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'label' should not be null", null),
                Arguments.of("'label' should not be empty", "  "),
                Arguments.of("'label' must be between 1 and 255 characters", invalidLengthValue)
        );
    }
}
