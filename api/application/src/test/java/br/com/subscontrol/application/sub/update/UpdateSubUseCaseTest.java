package br.com.subscontrol.application.sub.update;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.sub.Sub;
import br.com.subscontrol.domain.sub.SubGateway;
import br.com.subscontrol.domain.sub.SubID;
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

public class UpdateSubUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateSubUseCase useCase;

    @Mock
    private SubGateway gateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdate_shouldReturnId() {
        final var sub = Sub.create(UUID.randomUUID().toString(), "Muryllo Tiraza", "mail@mail.com");
        sleep();

        final var updatedAt = sub.getUpdatedAt();
        final var expectedId = sub.getId();
        final var expectedName = "Muryllo Tiraza Santos";
        final var expectedEmail = "muryllo.tiraza@mail.com";
        final var expectedActive = false;

        final var command = UpdateSubCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedEmail,
                expectedActive
        );

        when(gateway.findById(any())).thenReturn(Optional.of(sub));

        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(1)).update(argThat(updatedSub ->
                Objects.equals(expectedId, updatedSub.getId())
                        && Objects.equals(expectedName, updatedSub.getName())
                        && Objects.equals(expectedEmail, updatedSub.getEmail())
                        && Objects.equals(expectedActive, updatedSub.isActive())
                        && updatedAt.isBefore(updatedSub.getUpdatedAt())
                        && !Objects.isNull(updatedSub.getDeletedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = "123";

        final var command = UpdateSubCommand.with(
                expectedId,
                "Muryllo Tiraza",
                "mail@mail.com",
                true
        );

        when(gateway.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Sub with ID 123 was not found", exception.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(SubID.from(expectedId)));

        Mockito.verify(gateway, times(0)).update(any());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsUpdate_thenReceiveDomainException(String errorMessage, String name, String email) {
        final var content = Sub.create(UUID.randomUUID().toString(), "Muryllo Tiraza", "mail@mail.com");

        final var expectedId = content.getId();
        final var expectedIsActive = true;

        final var command = UpdateSubCommand.with(
                expectedId.getValue(),
                name,
                email,
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
