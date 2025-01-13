package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SubProviderTest {

    @Test
    void givenValidParameters_whenCallCreate_thenReturnAnInstance() {
        final var expectedType = SubProviderType.PATREON;
        final var expectedName = "Patreon Integration";
        final var expectedUrl = "";

        SubProvider provider = SubProvider.create(expectedType, expectedName, expectedUrl, null);

        assertNotNull(provider.getId());
        assertTrue(provider.isActive());
        assertEquals(expectedType, provider.getType());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedUrl, provider.getBaseUrl());
        assertNull(provider.getAuthentication());
    }

    @Test
    void givenValidParameters_whenCallWith_thenReturnAnInstance() {
        final var expectedId = SubProviderID.unique().getValue();
        final var expectedType = SubProviderType.PATREON;
        final var expectedName = "Patreon Integration";
        final var expectedUrl = "";
        final var expetedIsActive = true;
        final var expectedLastSync = InstantUtils.now();

        SubProvider provider = SubProvider.with(
                expectedId,
                expectedType,
                expectedName,
                expectedUrl,
                expetedIsActive,
                expectedLastSync,
                null
        );

        assertEquals(expectedId, provider.getId().getValue());
        assertEquals(expectedType, provider.getType());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedUrl, provider.getBaseUrl());
        assertEquals(expetedIsActive, provider.isActive());
        assertEquals(expectedLastSync, provider.getLastSync());
        assertNull(provider.getAuthentication());
    }

    @Test
    void givenValidInstance_whenCallUpdateWithValidParameters_thenUpdateInstance() {
        final var expectedType = SubProviderType.PATREON;
        final var expectedName = "Patreon Integration";
        final var expectedUrl = "";

        SubProvider provider = SubProvider.create(expectedType, expectedName, expectedUrl, null);

        assertNotNull(provider.getId());
        assertTrue(provider.isActive());
        assertEquals(expectedType, provider.getType());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedUrl, provider.getBaseUrl());
        assertNull(provider.getAuthentication());

        final var id = provider.getId().getValue();
        final var active = provider.isActive();
        final var type = provider.getType();
        final var name = "Google Drive";
        final var url = "http://www.patreon.com";
        provider.update(name, url, true);

        assertEquals(id, provider.getId().getValue());
        assertEquals(active, provider.isActive());
        assertEquals(type, provider.getType());
        assertEquals(name, provider.getName());
        assertEquals(url, provider.getBaseUrl());
    }

    @Test
    void givenActiveInstance_whenCallDeactivate_thenReturnInstanceDeactivated() {
        SubProvider provider = SubProvider.with(
                SubProviderID.unique().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "",
                true,
                null,
                null);

        assertTrue(provider.isActive());
        provider.deactivate();
        assertFalse(provider.isActive());
    }

    @Test
    void givenInactiveInstance_whenCallActivate_thenReturnInstanceActivated() {
        SubProvider provider = SubProvider.with(
                SubProviderID.unique().getValue(),
                SubProviderType.PATREON,
                "Patreon Integration",
                "",
                false,
                null,
                null);

        assertFalse(provider.isActive());
        provider.activate();
        assertTrue(provider.isActive());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidParameter_whenCallCreate_thenReceiveDomainException(String errorMessage, String name, String url) {
        DomainException exception = assertThrows(DomainException.class, () -> SubProvider.create(SubProviderType.PATREON, name, url, null));

        final var expectedMessage = "Failed to create Entity";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArguments() {
        final var name = "Patreon Integration";
        final var validUrl = "http://www.patreon.com";
        final var invalidUrl = "this_is_an_invalid_url!@#";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'name' should not be null", null, validUrl),
                Arguments.of("'name' should not be empty", "  ", validUrl),
                Arguments.of("'name' must be between 1 and 255 characters", invalidLengthValue, validUrl),
                Arguments.of("URL is not valid", name, invalidUrl)
        );
    }
}
