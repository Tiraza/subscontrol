package br.com.subscontrol.application.sub.create;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.sub.SubGateway;
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

public class CreateSubUseCaseTest extends UseCaseTest  {

    @InjectMocks
    private DefaultCreateSubUseCase useCase;

    @Mock
    private SubGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() {
        final var expectedProvidedId = UUID.randomUUID().toString();
        final var expectedName = "Muryllo Tiraza Santos";
        final var expectedEmail = "mail@mail.com";
        final var expectedIsActive = true;

        final var command = CreateSubCommand.with(
                expectedProvidedId,
                expectedName,
                expectedEmail
        );

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).create(argThat(sub ->
                Objects.equals(expectedProvidedId, sub.getProvidedId())
                        && Objects.equals(expectedName, sub.getName())
                        && Objects.equals(expectedEmail, sub.getEmail())
                        && Objects.equals(expectedIsActive, sub.isActive())
        ));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsCreate_thenReceiveDomainException(String errorMessage, String name, String email) {
        final var command = CreateSubCommand.with(UUID.randomUUID().toString(), name, email);
        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(0)).create(any());
    }

    private static Stream<Arguments> provideArguments() {
        final var validName = "Muryllo Tiraza Santos";
        final var validEmail = "mail@mail.com";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'name' should not be null", null, validEmail),
                Arguments.of("'name' should not be empty", "   ", validEmail),
                Arguments.of("'name' must be between 1 and 255 characters", invalidLengthValue, validEmail),
                Arguments.of("'email' should not be null", validName, null),
                Arguments.of("'email' should not be empty", validName, ""),
                Arguments.of("'email' must be between 1 and 255 characters", validName, invalidLengthValue)
        );
    }
}
