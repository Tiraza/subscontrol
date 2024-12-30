package br.com.subscontrol.application.tier.create;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.tier.TierGateway;
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

public class CreateTierUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateTierUseCase useCase;

    @Mock
    private TierGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() {
        final var subProviderID = SubProviderID.unique();
        final var expectedProvidedId = UUID.randomUUID().toString();
        final var expectedTitle = "Full Access";
        final var expectedDescription = "Access all contents";
        final var expectedAmount = "R$ 10,00";
        final var expectedIsActive = true;

        final var command = CreateTierCommand.with(
                subProviderID.getValue(),
                expectedProvidedId,
                expectedTitle,
                expectedDescription,
                expectedAmount
        );

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).create(argThat(tier ->
                Objects.equals(subProviderID, tier.getSubProviderID())
                        && Objects.equals(expectedProvidedId, tier.getProvidedId())
                        && Objects.equals(expectedTitle, tier.getTitle())
                        && Objects.equals(expectedDescription, tier.getDescription())
                        && Objects.equals(expectedAmount, tier.getAmount())
                        && Objects.equals(expectedIsActive, tier.isActive())
        ));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsCreate_thenReceiveDomainException(String errorMessage, String subProviderID, String title) {
        final var command = CreateTierCommand.with(
                subProviderID,
                UUID.randomUUID().toString(),
                title,
                "Access all contents",
                "R$ 10,00"
        );

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(0)).create(any());
    }

    private static Stream<Arguments> provideArguments() {
        final var validProviderID = SubProviderID.unique().toString();
        final var validTitle = "Full Access";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'title' should not be null", validProviderID, null),
                Arguments.of("'title' should not be empty", validProviderID, "  "),
                Arguments.of("'title' must be between 1 and 255 characters", validProviderID, invalidLengthValue),
                Arguments.of("'ProviderID' should not be empty", "   ", validTitle)
        );
    }
}
