package br.com.subscontrol.application.content.create;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.exceptions.DomainException;
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
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CreateContentUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateContentUseCase useCase;

    @Mock
    private ContentGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() {
        final var expectedLabel = "Shared Folder";
        final var expectedProvidedId = UUID.randomUUID().toString();
        final var expectedIsActive = true;

        final var command = CreateContentCommand.with(
                expectedProvidedId,
                expectedLabel
        );

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).create(argThat(content ->
                Objects.equals(expectedProvidedId, content.getProvidedId())
                        && Objects.equals(expectedLabel, content.getLabel())
                        && Objects.equals(expectedIsActive, content.isActive())
        ));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsCreate_thenReceiveDomainException(String errorMessage, String providedId, String label) {
        final var command = CreateContentCommand.with(providedId, label);
        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(0)).create(any());
    }

    private static Stream<Arguments> provideArguments() {
        final var providedId = UUID.randomUUID().toString();
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'label' should not be null", providedId, null),
                Arguments.of("'label' should not be empty", providedId, " "),
                Arguments.of("'label' must be between 1 and 255 characters", providedId, invalidLengthValue)
        );
    }
}
