package br.com.subscontrol.domain.provider.authentication;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.provider.ProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationTest {

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
    @MethodSource("expectedIds")
    void givenInvalidParameters_whenCallUpdateFile_thenReceiveDomainException(final ProviderID expectedProviderID) {
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
        final var expectedErrorMessage = "'file' should not be null";

        DomainException exception = assertThrows(DomainException.class, () -> instance.updateFile(null));

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
}
