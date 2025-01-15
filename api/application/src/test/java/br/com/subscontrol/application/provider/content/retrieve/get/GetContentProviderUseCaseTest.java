package br.com.subscontrol.application.provider.content.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GetContentProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetContentProviderUseCase useCase;

    @Mock
    private ContentProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE.getName();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var provider = ContentProvider.create(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));

        final var actualProvider = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualProvider.id());
        assertEquals(expectedType, actualProvider.type());
        assertEquals(expectedName, actualProvider.name());
        assertEquals(expectedBaseUrl, actualProvider.baseUrl());
        assertEquals(expectedIsActive, actualProvider.isActive());
        assertEquals(expectedAuthenticationType.name(), actualProvider.authenticationType());
        assertEquals(expectedClientId, actualProvider.clientId());
        assertEquals(expectedClientSecret, actualProvider.clientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.authorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.tokenUrl());

        verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "ContentProvider with ID 123 was not found";
        final var expectedId = ContentProviderID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId));
    }
}
