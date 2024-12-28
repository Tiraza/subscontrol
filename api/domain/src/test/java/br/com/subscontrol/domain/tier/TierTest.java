package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.content.ContentID;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.sub.SubID;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.utils.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TierTest {

    @Test
    void givenValidParameters_whenCallCreate() {
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var expectedTitle = "Assinante Basico";
        final var expectedDescription = "Modalidade basica de assinatura";
        final var expectedAmount = "R$ 10,00";

        Tier tier = Tier.create(providerID, providedId, expectedTitle, expectedDescription, expectedAmount);

        assertNotNull(tier.getId());
        assertNotNull(tier.getCreatedAt());
        assertNotNull(tier.getUpdatedAt());
        assertNull(tier.getDeletedAt());
        assertEquals(providerID, tier.getSubProviderID().getValue());
        assertEquals(providedId, tier.getProvidedId());
        assertEquals(expectedTitle, tier.getTitle());
        assertEquals(expectedDescription, tier.getDescription());
        assertEquals(expectedAmount, tier.getAmount());
        assertTrue(tier.getSubTiers().isEmpty());
        assertTrue(tier.getSubscribers().isEmpty());
        assertTrue(tier.getContents().isEmpty());
    }

    @Test
    void givenValidParameters_whenCallWith_thenInstantiateATier() {
        final var id = TierID.unique().getValue();
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var expectedTitle = "Assinante Basico";
        final var expectedDescription = "Modalidade basica de assinatura";
        final var expectedAmount = "R$ 10,00";
        final var expectedSubTiers = List.of(TierID.unique());
        final var expectedSubscribers = List.of(SubID.unique());
        final var expectedContents = List.of(ContentID.unique());
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Tier tier = Tier.with(
                id,
                providerID,
                providedId,
                expectedTitle,
                expectedDescription,
                expectedAmount,
                expectedSubTiers,
                expectedSubscribers,
                expectedContents,
                isActive,
                createdAt,
                updatedAt,
                deletedAt);

        assertEquals(id, tier.getId().getValue());
        assertEquals(providerID, tier.getSubProviderID().getValue());
        assertEquals(providedId, tier.getProvidedId());
        assertEquals(expectedTitle, tier.getTitle());
        assertEquals(expectedDescription, tier.getDescription());
        assertEquals(expectedAmount, tier.getAmount());
        assertEquals(createdAt, tier.getCreatedAt());
        assertEquals(updatedAt, tier.getUpdatedAt());
        assertEquals(expectedSubTiers, tier.getSubTiers());
        assertEquals(expectedSubscribers, tier.getSubscribers());
        assertEquals(expectedContents, tier.getContents());
        assertNull(tier.getDeletedAt());
    }

    @Test
    void givenAValidTier_whenCallUpdateWithValidParameters_shouldReceiveTierUpdated() {
        final var id = TierID.unique().getValue();
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var title = "Assinante Basico";
        final var description = "Modalidade basica de assinatura";
        final var amount = "R$ 10,00";
        final var subTiers = List.of(TierID.from("123"));
        final var subscribers = List.of(SubID.from("123"));
        final var contents = List.of(ContentID.from("123"));
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Tier tier = Tier.with(
                id,
                providerID,
                providedId,
                title,
                description,
                amount,
                subTiers,
                subscribers,
                contents,
                isActive,
                createdAt,
                updatedAt,
                deletedAt);

        assertEquals(id, tier.getId().getValue());
        assertEquals(providerID, tier.getSubProviderID().getValue());
        assertEquals(providedId, tier.getProvidedId());
        assertEquals(title, tier.getTitle());
        assertEquals(description, tier.getDescription());
        assertEquals(amount, tier.getAmount());
        assertEquals(createdAt, tier.getCreatedAt());
        assertEquals(updatedAt, tier.getUpdatedAt());
        assertEquals(subTiers, tier.getSubTiers());
        assertEquals(subscribers, tier.getSubscribers());
        assertEquals(contents, tier.getContents());
        assertNull(tier.getDeletedAt());

        ThreadUtils.sleep();
        final var expectedTitle = "Assinante Total";
        final var expectedDescription = "Modalidade com acesso total";
        final var expectedAmount = "R$ 50,00";
        final var expectedSubTiers = List.of(TierID.unique());
        final var expectedSubscribers = List.of(SubID.unique());
        final var expectedContents = List.of(ContentID.unique());

        tier.update(expectedTitle, expectedDescription, expectedAmount, expectedSubTiers, expectedSubscribers, expectedContents, isActive);

        assertEquals(expectedTitle, tier.getTitle());
        assertEquals(expectedDescription, tier.getDescription());
        assertEquals(expectedAmount, tier.getAmount());
        assertEquals(createdAt, tier.getCreatedAt());
        assertEquals(expectedSubTiers, tier.getSubTiers());
        assertEquals(expectedSubscribers, tier.getSubscribers());
        assertEquals(expectedContents, tier.getContents());
        assertNull(tier.getDeletedAt());
        assertTrue(updatedAt.isBefore(tier.getUpdatedAt()));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForCreate")
    void givenInvalidParameter_whenCallCreate_thenReceiveDomainException(String errorMessage, String providerID, String title) {
        final var providedId = UUID.randomUUID().toString();
        final var expectedDescription = "Modalidade basica de assinatura";
        final var expectedAmount = "R$ 10,00";

        DomainException exception = assertThrows(DomainException.class, () -> Tier.create(providerID, providedId, title, expectedDescription, expectedAmount));

        final var expectedMessage = "Failed to create Entity";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArgumentsForCreate() {
        final var validProviderID = UUID.randomUUID().toString();
        final var validTitle = "Assinante Total";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'title' should not be null", validProviderID, null),
                Arguments.of("'title' should not be empty", validProviderID, "  "),
                Arguments.of("'title' must be between 1 and 255 characters", validProviderID, invalidLengthValue),
                Arguments.of("'ProviderID' should not be empty", "   ", validTitle)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForWith")
    void givenInvalidParameter_whenCallWIth_thenReceiveDomainException(String errorMessage, TierID id, String providerID, String title, List<TierID> subTiers) {
        DomainException exception = assertThrows(DomainException.class, () -> Tier.with(
                id.getValue(),
                providerID,
                UUID.randomUUID().toString(),
                title,
                "Modalidade basica de assinatura",
                "R$ 10,00",
                subTiers,
                null,
                null,
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null));

        final var expectedMessage = "Failed to create Entity";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArgumentsForWith() {
        final var tierID = TierID.unique();
        final var validProviderID = UUID.randomUUID().toString();
        final var validTitle = "Assinante Total";
        final var validSubTiers = List.of(TierID.from("123"), TierID.from("321"));
        final var invalidSubTiers = List.of(tierID);
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'title' should not be null", tierID, validProviderID, null, validSubTiers),
                Arguments.of("'title' should not be empty", tierID, validProviderID, "  ", validSubTiers),
                Arguments.of("'title' must be between 1 and 255 characters", tierID, validProviderID, invalidLengthValue, validSubTiers),
                Arguments.of("cyclic dependency in tier", tierID, validProviderID, validTitle, invalidSubTiers),
                Arguments.of("'ProviderID' should not be empty", tierID, "  ", validTitle, validSubTiers)
        );
    }
}
