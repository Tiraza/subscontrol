package br.com.subscontrol.domain.provider.authentication;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.exceptions.UnsupportedAuthenticationType;
import br.com.subscontrol.domain.provider.ProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationTest {

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenValidParametersWithTypeClientSecret_whenCallCreate_thenReturnAnInstance(final ProviderID expectedProviderID) {
        final var expectedType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthenticationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var instance = Authentication.create(
                expectedProviderID,
                expectedType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthenticationUrl,
                expectedTokenUrl,
                null
        );

        assertEquals(expectedType, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertEquals(expectedClientId, instance.getClientId());
        assertEquals(expectedClientSecret, instance.getClientSecret());
        assertEquals(expectedAuthenticationUrl, instance.getAuthorizationUrl());
        assertEquals(expectedTokenUrl, instance.getTokenUrl());
        assertNull(instance.getFile());
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenValidParametersWithTypeFile_whenCallCreate_thenReturnAnInstance(final ProviderID expectedProviderID) {
        final var expectedType = AuthenticationType.FILE;
        final var expectedFile = Base64.getEncoder().encodeToString("teste".getBytes());

        final var instance = Authentication.create(
                expectedProviderID,
                expectedType.name(),
                null,
                null,
                null,
                null,
                expectedFile
        );

        assertEquals(expectedType, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertNotNull(instance.getFile());
        assertNull(instance.getClientId());
        assertNull(instance.getClientSecret());
        assertNull(instance.getAuthorizationUrl());
        assertNull(instance.getTokenUrl());
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenInvalidTypeThatDoesNotExists_whenCallCreate_thenReceiveNotFoundException(final ProviderID expectedProviderID) {
        final var invalidType = mock(AuthenticationType.class);
        when(invalidType.name()).thenReturn("INVALID");

        final var exception = assertThrows(NotFoundException.class, () -> {
            Authentication.create(
                    expectedProviderID,
                    invalidType.name(),
                    null,
                    null,
                    null,
                    null,
                    null
            );
        });

        assertEquals("AuthenticationType with value INVALID was not found", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenInvalidTypeThatExistsButIsNotSupported_whenCallCreate_thenReceiveUnsupportedAuthenticationTypeException(final ProviderID expectedProviderID) {
        try (MockedStatic<AuthenticationType> mockStatic = mockStatic(AuthenticationType.class)) {
            final var invalidType = mock(AuthenticationType.class);
            when(invalidType.name()).thenReturn("INVALID");

            mockStatic.when(() -> AuthenticationType.from(invalidType.name())).thenReturn(invalidType);

            final var exception = assertThrows(UnsupportedAuthenticationType.class, () -> {
                Authentication.create(
                        expectedProviderID,
                        invalidType.name(),
                        null,
                        null,
                        null,
                        null,
                        null
                );
            });


            assertEquals("Unsupported authentication type 'INVALID'", exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenValidParameters_whenCallWithClientSecret_thenReturnAnInstance(final ProviderID expectedProviderID) {
        final var expectedType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthenticationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var instance = Authentication.withClientSecret(
                expectedProviderID,
                expectedClientId,
                expectedClientSecret,
                expectedAuthenticationUrl,
                expectedTokenUrl);

        assertEquals(expectedType, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertEquals(expectedClientId, instance.getClientId());
        assertEquals(expectedClientSecret, instance.getClientSecret());
        assertEquals(expectedAuthenticationUrl, instance.getAuthorizationUrl());
        assertEquals(expectedTokenUrl, instance.getTokenUrl());
        assertNull(instance.getFile());
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenValidParameters_whenCallWithClientFile_thenReturnAnInstance(final ProviderID expectedProviderID) {
        final var expectedType = AuthenticationType.FILE;
        final var expectedFile = new byte[1];

        final var instance = Authentication.withFile(expectedProviderID, expectedFile);

        assertEquals(expectedType, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertEquals(expectedFile, instance.getFile());
        assertNull(instance.getClientId());
        assertNull(instance.getClientSecret());
        assertNull(instance.getAuthorizationUrl());
        assertNull(instance.getTokenUrl());
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenValidParameters_whenCallUpdateClientSecret_thenUpdateInstance(final ProviderID expectedProviderID) {
        final var instance = Authentication.withFile(expectedProviderID, new byte[1]);

        assertEquals(expectedProviderID, instance.getProviderID());
        assertEquals(AuthenticationType.FILE, instance.getType());
        assertNotNull(instance.getFile());
        assertNull(instance.getClientId());
        assertNull(instance.getClientSecret());
        assertNull(instance.getAuthorizationUrl());
        assertNull(instance.getTokenUrl());

        final var expectedType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthenticationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        instance.updateClientSecret(
                expectedClientId,
                expectedClientSecret,
                expectedAuthenticationUrl,
                expectedTokenUrl);

        assertEquals(expectedType, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertEquals(expectedClientId, instance.getClientId());
        assertEquals(expectedClientSecret, instance.getClientSecret());
        assertEquals(expectedAuthenticationUrl, instance.getAuthorizationUrl());
        assertEquals(expectedTokenUrl, instance.getTokenUrl());
        assertNull(instance.getFile());
    }

    @ParameterizedTest
    @MethodSource("expectedIds")
    void givenValidParameters_whenCallUpdateFile_thenUpdateInstance(final ProviderID expectedProviderID) {
        final var instance = Authentication.withClientSecret(
                expectedProviderID,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token");

        assertEquals(AuthenticationType.CLIENT_SECRET, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertNotNull(instance.getClientId());
        assertNotNull(instance.getClientSecret());
        assertNotNull(instance.getAuthorizationUrl());
        assertNotNull(instance.getTokenUrl());
        assertNull(instance.getFile());

        final var expectedType = AuthenticationType.FILE;
        final var expectedFile = new byte[1];

        instance.updateFile(expectedFile);

        assertEquals(expectedType, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertEquals(expectedFile, instance.getFile());
        assertNull(instance.getClientId());
        assertNull(instance.getClientSecret());
        assertNull(instance.getAuthorizationUrl());
        assertNull(instance.getTokenUrl());
    }

    @ParameterizedTest
    @MethodSource("expectedClientSecret")
    void givenInvalidParameters_whenCallUpdateClientSecret_thenReceiveDomainException(
            final ProviderID expectedProviderID,
            final String expectedErrorMessage,
            final String clientId,
            final String clientSecret,
            final String authorizationUrl,
            final String tokenUrl
    ) {
        final var instance = Authentication.withClientSecret(
                expectedProviderID,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token");

        assertEquals(AuthenticationType.CLIENT_SECRET, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertNotNull(instance.getClientId());
        assertNotNull(instance.getClientSecret());
        assertNotNull(instance.getAuthorizationUrl());
        assertNotNull(instance.getTokenUrl());
        assertNull(instance.getFile());

        final var expectedMessage = "Failed to create ValueObject";
        final int expectedErrorCount = 1;

        DomainException exception = assertThrows(
                DomainException.class,
                () -> instance.updateClientSecret(clientId, clientSecret, authorizationUrl, tokenUrl)
        );

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @ParameterizedTest
    @MethodSource("expectedFiles")
    void givenInvalidParameters_whenCallUpdateFile_thenReceiveDomainException(
            final ProviderID expectedProviderID,
            final String expectedErrorMessage,
            final byte[] file
    ) {
        final var instance = Authentication.withClientSecret(
                expectedProviderID,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token");

        assertEquals(AuthenticationType.CLIENT_SECRET, instance.getType());
        assertEquals(expectedProviderID, instance.getProviderID());
        assertNotNull(instance.getClientId());
        assertNotNull(instance.getClientSecret());
        assertNotNull(instance.getAuthorizationUrl());
        assertNotNull(instance.getTokenUrl());
        assertNull(instance.getFile());

        final var expectedMessage = "Failed to create ValueObject";
        final int expectedErrorCount = 1;

        DomainException exception = assertThrows(DomainException.class, () -> instance.updateFile(file));

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> expectedIds() {
        return Stream.of(
                Arguments.of(SubProviderID.unique()),
                Arguments.of(ContentProviderID.unique())
        );
    }

    private static Stream<Arguments> expectedFiles() {
        return Stream.of(
                Arguments.of(SubProviderID.unique(), "'file' should not be null", null),
                Arguments.of(SubProviderID.unique(), "'file' should not be empty", new byte[0]),
                Arguments.of(ContentProviderID.unique(), "'file' should not be null", null),
                Arguments.of(ContentProviderID.unique(), "'file' should not be empty", new byte[0])
        );
    }

    private static Stream<Arguments> expectedClientSecret() {
        final var validClientId = UUID.randomUUID().toString();
        final var validClientSecret = UUID.randomUUID().toString();
        final var validAuthorizationUrl = "/authorization";
        final var validTokenUrl = "/token";

        final String invalidNullValue = null;
        final var invalidEmptyValue = "  ";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of(ContentProviderID.unique(), "'clientId' should not be null", invalidNullValue, validClientSecret, validAuthorizationUrl, validTokenUrl),
                Arguments.of(SubProviderID.unique(), "'clientId' should not be empty", invalidEmptyValue, validClientSecret, validAuthorizationUrl, validTokenUrl),
                Arguments.of(ContentProviderID.unique(), "'clientId' must be between 1 and 255 characters", invalidLengthValue, validClientSecret, validAuthorizationUrl, validTokenUrl),
                Arguments.of(SubProviderID.unique(), "'clientSecret' should not be null", validClientId, invalidNullValue, validAuthorizationUrl, validTokenUrl),
                Arguments.of(ContentProviderID.unique(), "'clientSecret' should not be empty", validClientId, invalidEmptyValue, validAuthorizationUrl, validTokenUrl),
                Arguments.of(SubProviderID.unique(), "'clientSecret' must be between 1 and 255 characters", validClientId, invalidLengthValue, validAuthorizationUrl, validTokenUrl),
                Arguments.of(ContentProviderID.unique(), "'authorizationUrl' should not be null", validClientId, validClientSecret, invalidNullValue, validTokenUrl),
                Arguments.of(SubProviderID.unique(), "'authorizationUrl' should not be empty", validClientId, validClientSecret, invalidEmptyValue, validTokenUrl),
                Arguments.of(ContentProviderID.unique(), "'authorizationUrl' must be between 1 and 255 characters", validClientId, validClientSecret, invalidLengthValue, validTokenUrl),
                Arguments.of(SubProviderID.unique(), "'tokenUrl' should not be null", validClientId, validClientSecret, validAuthorizationUrl, invalidNullValue),
                Arguments.of(ContentProviderID.unique(), "'tokenUrl' should not be empty", validClientId, validClientSecret, validAuthorizationUrl, invalidEmptyValue),
                Arguments.of(SubProviderID.unique(), "'tokenUrl' must be between 1 and 255 characters", validClientId, validClientSecret, validAuthorizationUrl, invalidLengthValue)
        );
    }
}
