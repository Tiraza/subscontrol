package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.utils.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TierTest {

    @Test
    void givenValidParameters_whenCallNewTier_thenInstantiateATier() {
        final var providedId = UUID.randomUUID().toString();
        final var expectedTitle = "Assinante Basico";
        final var expectedDescription = "Modalidade basica de assinatura";
        final var expectedAmount = "R$ 10,00";

        Tier tier = Tier.newTier(providedId, expectedTitle, expectedDescription, expectedAmount);

        assertNotNull(tier.getId());
        assertNotNull(tier.getCreatedAt());
        assertNotNull(tier.getUpdatedAt());
        assertNull(tier.getDeletedAt());
        assertEquals(providedId, tier.getProvidedId());
        assertEquals(expectedTitle, tier.getTitle());
        assertEquals(expectedDescription, tier.getDescription());
        assertEquals(expectedAmount, tier.getAmount());
    }

    @Test
    void givenValidParameters_whenCallWith_thenInstantiateATier() {
        final var id = TierID.unique().getValue();
        final var providedId = UUID.randomUUID().toString();
        final var expectedTitle = "Assinante Basico";
        final var expectedDescription = "Modalidade basica de assinatura";
        final var expectedAmount = "R$ 10,00";
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Tier tier = Tier.with(id, providedId, expectedTitle, expectedDescription, expectedAmount, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, tier.getId().getValue());
        assertEquals(providedId, tier.getProvidedId());
        assertEquals(expectedTitle, tier.getTitle());
        assertEquals(expectedDescription, tier.getDescription());
        assertEquals(expectedAmount, tier.getAmount());
        assertEquals(createdAt, tier.getCreatedAt());
        assertEquals(updatedAt, tier.getUpdatedAt());
        assertNull(tier.getDeletedAt());
    }

    @Test
    void givenAValidTier_whenCallUpdateWithValidParameters_shouldReceiveTierUpdated() {
        final var id = TierID.unique().getValue();
        final var providedId = UUID.randomUUID().toString();
        final var title = "Assinante Basico";
        final var description = "Modalidade basica de assinatura";
        final var amount = "R$ 10,00";
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Tier tier = Tier.with(id, providedId, title, description, amount, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, tier.getId().getValue());
        assertEquals(providedId, tier.getProvidedId());
        assertEquals(title, tier.getTitle());
        assertEquals(description, tier.getDescription());
        assertEquals(amount, tier.getAmount());
        assertEquals(createdAt, tier.getCreatedAt());
        assertEquals(updatedAt, tier.getUpdatedAt());
        assertNull(tier.getDeletedAt());

        ThreadUtils.sleep();
        final var expectedTitle = "Assinante Total";
        final var expectedDescription = "Modalidade com acesso total";
        final var expectedAmount = "R$ 50,00";
        tier.update(expectedTitle, expectedDescription, expectedAmount, isActive);

        assertEquals(expectedTitle, tier.getTitle());
        assertEquals(expectedDescription, tier.getDescription());
        assertEquals(expectedAmount, tier.getAmount());
        assertEquals(createdAt, tier.getCreatedAt());
        assertNull(tier.getDeletedAt());
        assertTrue(updatedAt.isBefore(tier.getUpdatedAt()));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidParameter_whenCallNewTier_thenReceiveDomainException(String errorMessage, String title) {
        final var providedId = UUID.randomUUID().toString();
        final var expectedDescription = "Modalidade basica de assinatura";
        final var expectedAmount = "R$ 10,00";

        DomainException exception = assertThrows(DomainException.class, () -> Tier.newTier(providedId, title, expectedDescription, expectedAmount));

        final var expectedMessage = "Failed to create Entity Tier";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArguments() {
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'title' should not be null", null),
                Arguments.of("'title' should not be empty", "  "),
                Arguments.of("'title' must be between 1 and 255 characters", invalidLengthValue)
        );
    }
}
