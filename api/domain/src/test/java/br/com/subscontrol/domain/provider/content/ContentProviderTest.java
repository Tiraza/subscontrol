package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ContentProviderTest {

    @Test
    void givenValidParameters_whenCallCreate_thenReturnAnInstance() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE;
        final var expectedName = "Google Drive Integration";
        final var expectedUrl = "";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        ContentProvider provider = ContentProvider.create(
                expectedType.getName(),
                expectedName,
                expectedUrl,
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        assertNotNull(provider.getId());
        assertTrue(provider.isActive());
        assertEquals(expectedType, provider.getType());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedUrl, provider.getBaseUrl());

        Authentication authentication = provider.getAuthentication();
        assertNotNull(authentication);
        assertEquals(expectedAuthenticationType, authentication.getType());
        assertEquals(expectedClientId, authentication.getClientId());
        assertEquals(expectedClientSecret, authentication.getClientSecret());
        assertEquals(expectedAuthorizationUrl, authentication.getAuthorizationUrl());
        assertEquals(expectedTokenUrl, authentication.getTokenUrl());
        assertNull(authentication.getFile());
    }

    @Test
    void givenValidParameters_whenCallWith_thenReturnAnInstance() {
        final var expectedId = ContentProviderID.unique();
        final var expectedType = ContentProviderType.GOOGLE_DRIVE;
        final var expectedName = "Google Drive Integration";
        final var expectedUrl = "";
        final var expetedIsActive = true;
        final var expectedLastSync = InstantUtils.now();
        final var expectedAuthentication = Authentication.withFile(expectedId, new byte[1]);

        ContentProvider provider = ContentProvider.with(
                expectedId.getValue(),
                expectedType,
                expectedName,
                expectedUrl,
                expetedIsActive,
                expectedLastSync,
                expectedAuthentication
        );

        assertEquals(expectedId, provider.getId());
        assertEquals(expectedType, provider.getType());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedUrl, provider.getBaseUrl());
        assertEquals(expetedIsActive, provider.isActive());
        assertEquals(expectedLastSync, provider.getLastSync());
        assertEquals(expectedAuthentication, provider.getAuthentication());
    }

    @Test
    void givenValidInstance_whenCallUpdateWithValidParameters_thenUpdateInstance() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE;
        final var expectedName = "Google Drive Integration";
        final var expectedUrl = "";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        ContentProvider provider = ContentProvider.create(
                expectedType.getName(),
                expectedName,
                expectedUrl,
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        assertNotNull(provider.getId());
        assertTrue(provider.isActive());
        assertEquals(expectedType, provider.getType());
        assertEquals(expectedName, provider.getName());
        assertEquals(expectedUrl, provider.getBaseUrl());

        Authentication authentication = provider.getAuthentication();
        assertNotNull(authentication);
        assertEquals(expectedAuthenticationType, authentication.getType());
        assertEquals(expectedClientId, authentication.getClientId());
        assertEquals(expectedClientSecret, authentication.getClientSecret());
        assertEquals(expectedAuthorizationUrl, authentication.getAuthorizationUrl());
        assertEquals(expectedTokenUrl, authentication.getTokenUrl());
        assertNull(authentication.getFile());

        final var id = provider.getId().getValue();
        final var active = provider.isActive();
        final var type = provider.getType();
        final var name = "Google Drive";
        final var url = "https://google.com";
        provider.update(name, url, true);

        assertEquals(id, provider.getId().getValue());
        assertEquals(active, provider.isActive());
        assertEquals(type, provider.getType());
        assertEquals(name, provider.getName());
        assertEquals(url, provider.getBaseUrl());
    }

    @Test
    void givenActiveInstance_whenCallDeactivate_thenReturnInstanceDeactivated() {
        ContentProviderID providerID = ContentProviderID.unique();
        ContentProvider provider = ContentProvider.with(
                providerID.getValue(),
                ContentProviderType.GOOGLE_DRIVE,
                "Google Drive Integration",
                "",
                true,
                null,
                Authentication.withFile(providerID, new byte[1]));

        assertTrue(provider.isActive());
        provider.deactivate();
        assertFalse(provider.isActive());
    }

    @Test
    void givenInactiveInstance_whenCallActivate_thenReturnInstanceActivated() {
        ContentProviderID providerID = ContentProviderID.unique();
        ContentProvider provider = ContentProvider.with(
                ContentProviderID.unique().getValue(),
                ContentProviderType.GOOGLE_DRIVE,
                "Google Drive Integration",
                "",
                false,
                null,
                Authentication.withFile(providerID, new byte[1]));

        assertFalse(provider.isActive());
        provider.activate();
        assertTrue(provider.isActive());
    }

    @Test
    void givenInvalidNullAuthentication_whenCallWith_thenReceiveDomainException() {
        DomainException exception = assertThrows(DomainException.class, () -> {
            ContentProvider.with(
                    ContentProviderID.unique().getValue(),
                    ContentProviderType.GOOGLE_DRIVE,
                    "Patreon",
                    "http://www.patreon.com",
                    true,
                    InstantUtils.now(),
                    null
            );
        });

        final var expectedMessage = "Failed to create Entity";
        final int expectedErrorCount = 1;
        final var expectedErroMessage = "A provider must have authentication";

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErroMessage, exception.firstError().message());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidParameter_whenCallCreate_thenReceiveDomainException(String errorMessage, String name, String url) {
        DomainException exception = assertThrows(DomainException.class, () -> {
            ContentProvider.create(
                    ContentProviderType.GOOGLE_DRIVE.getName(),
                    name,
                    url,
                    AuthenticationType.CLIENT_SECRET.name(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    "/authorization",
                    "/token",
                    null);
        });

        final var expectedMessage = "Failed to create Entity";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArguments() {
        final var name = "Google Drive Integration";
        final var validUrl = "http://www.google.com";
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
