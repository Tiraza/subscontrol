package br.com.subscontrol.domain.content;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.utils.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ContentTest {

    @Test
    void givenValidParameters_whenCallCreate_thenInstantiateAContent() {
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var label = "Content Label";

        Content content = Content.create(providerID, providedId, label);

        assertNotNull(content.getId());
        assertEquals(providerID, content.getContentProviderID().getValue());
        assertEquals(providedId, content.getProvidedId());
        assertEquals(label, content.getLabel());
        assertTrue(content.isActive());
        assertNotNull(content.getCreatedAt());
        assertNotNull(content.getUpdatedAt());
        assertNull(content.getDeletedAt());
    }

    @Test
    void givenValidParameters_whenCallWith_thenInstantiateAContent() {
        final var id = ContentID.unique().getValue();
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var label = "Content Label";
        final var isActive = false;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Content content = Content.with(id, providerID, providedId, label, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, content.getId().getValue());
        assertEquals(providerID, content.getContentProviderID().getValue());
        assertEquals(providedId, content.getProvidedId());
        assertEquals(label, content.getLabel());
        assertEquals(isActive, content.isActive());
        assertEquals(createdAt, content.getCreatedAt());
        assertEquals(updatedAt, content.getUpdatedAt());
        assertEquals(deletedAt, content.getDeletedAt());
    }

    @Test
    void givenActiveInstance_whenCallDeactivate_thenReturnInstanceDeactivated() {
        final var id = ContentID.unique().getValue();
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var label = "Content Label";
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Content content = Content.with(id, providerID, providedId, label, isActive, createdAt, updatedAt, deletedAt);

        assertTrue(content.isActive());

        ThreadUtils.sleep();
        content.deactivate();

        assertFalse(content.isActive());
        assertTrue(updatedAt.isBefore(content.getUpdatedAt()));
    }

    @Test
    void givenInactiveInstance_whenCallActivate_thenReturnInstanceActivated() {
        final var id = ContentID.unique().getValue();
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var label = "Content Label";
        final var isActive = false;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Content content = Content.with(id, providerID, providedId, label, isActive, createdAt, updatedAt, deletedAt);

        assertFalse(content.isActive());

        ThreadUtils.sleep();
        content.activate();

        assertTrue(content.isActive());
        assertTrue(updatedAt.isBefore(content.getUpdatedAt()));
    }

    @Test
    void givenValidInstance_whenCallUpdateWithValidParameters_thenUpdateInstance() {
        final var id = ContentID.unique().getValue();
        final var providerID = UUID.randomUUID().toString();
        final var providedId = UUID.randomUUID().toString();
        final var label = "Content Label";
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Content content = Content.with(id, providerID, providedId, label, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, content.getId().getValue());
        assertEquals(providedId, content.getProvidedId());
        assertEquals(label, content.getLabel());
        assertEquals(isActive, content.isActive());
        assertEquals(createdAt, content.getCreatedAt());
        assertEquals(updatedAt, content.getUpdatedAt());

        ThreadUtils.sleep();
        final var expectedLabel = "New Content Label";
        content.update(expectedLabel, false);

        assertEquals(id, content.getId().getValue());
        assertEquals(expectedLabel, content.getLabel());
        assertFalse(content.isActive());
        assertEquals(createdAt, content.getCreatedAt());
        assertTrue(updatedAt.isBefore(content.getUpdatedAt()));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidParameter_whenCallCreate_thenReceiveDomainException(String errorMessage, String providerID, String label) {
        final var providedId = UUID.randomUUID().toString();
        DomainException exception = assertThrows(DomainException.class, () -> Content.create(providerID, providedId, label));

        final var expectedMessage = "Failed to create Entity";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArguments() {
        final var validProviderID = UUID.randomUUID().toString();
        final var validLabel = "Content Label";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'label' should not be null", validProviderID, null),
                Arguments.of("'label' should not be empty", validProviderID, "  "),
                Arguments.of("'label' must be between 1 and 255 characters", validProviderID, invalidLengthValue),
                Arguments.of("'ProviderID' should not be empty", "  ", validLabel)
        );
    }
}

