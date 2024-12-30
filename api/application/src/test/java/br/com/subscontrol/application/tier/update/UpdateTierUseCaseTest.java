package br.com.subscontrol.application.tier.update;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.tier.Tier;
import br.com.subscontrol.domain.tier.TierGateway;
import br.com.subscontrol.domain.tier.TierID;
import br.com.subscontrol.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateTierUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateTierUseCase useCase;

    @Mock
    private TierGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdate_shouldReturnId() {
        final var tier = Tier.create(
                SubProviderID.unique().getValue(),
                UUID.randomUUID().toString(),
                "Minor Access",
                "Access minor contents",
                "R$ 5,00"
        );
        sleep();

        final var updatedAt = tier.getUpdatedAt();
        final var expectedId = tier.getId();
        final var expectedTitle = "Full Access";
        final var expectedDescription = "Access all contents";
        final var expectedAmount = "R$ 10,00";
        final var expectedActive = false;

        final var command = UpdateTierCommand.with(
                expectedId.getValue(),
                expectedTitle,
                expectedDescription,
                expectedAmount,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                expectedActive
        );

        when(gateway.findById(any())).thenReturn(Optional.of(tier));

        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertEquals(expectedId.getValue(), output.id());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(1)).update(argThat(updatedTier ->
                Objects.equals(expectedId, updatedTier.getId())
                        && Objects.equals(expectedTitle, updatedTier.getTitle())
                        && Objects.equals(expectedDescription, updatedTier.getDescription())
                        && Objects.equals(expectedAmount, updatedTier.getAmount())
                        && Objects.equals(expectedActive, updatedTier.isActive())
                        && updatedAt.isBefore(updatedTier.getUpdatedAt())
                        && !Objects.isNull(updatedTier.getDeletedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = "123";

        final var command = UpdateTierCommand.with(
                expectedId,
                "Full Access",
                "Access all contents",
                "R$ 10,00",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                true
        );

        when(gateway.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Tier with ID 123 was not found", exception.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(TierID.from(expectedId)));

        Mockito.verify(gateway, times(0)).update(any());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsUpdate_thenReceiveDomainException(String errorMessage, TierID id, String title, List<TierID> subTiers) {
        final var tier = Tier.with(
                id.getValue(),
                SubProviderID.unique().getValue(),
                UUID.randomUUID().toString(),
                "Default",
                "Access all contents",
                "R$ 10,00",
                null,
                null,
                null,
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        final var expectedId = tier.getId();

        final var command = UpdateTierCommand.with(
                expectedId.getValue(),
                title,
                "Access all contents",
                "R$ 10,00",
                subTiers.stream().map(TierID::getValue).toList(),
                new ArrayList<>(),
                new ArrayList<>(),
                true
        );

        when(gateway.findById(any())).thenReturn(Optional.of(tier));

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(0)).update(any());
    }

    private static Stream<Arguments> provideArguments() {
        final var tierID = TierID.unique();
        final var validTitle = "Assinante Total";
        final var validSubTiers = List.of(TierID.from("123"), TierID.from("321"));
        final var invalidSubTiers = List.of(tierID);
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'title' should not be null", tierID, null, validSubTiers),
                Arguments.of("'title' should not be empty", tierID, "  ", validSubTiers),
                Arguments.of("'title' must be between 1 and 255 characters", tierID, invalidLengthValue, validSubTiers),
                Arguments.of("cyclic dependency in tier", tierID, validTitle, invalidSubTiers)
        );
    }

}
