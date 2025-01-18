package br.com.subscontrol.application.provider.sub.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
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

class GetSubProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetSubProviderUseCase useCase;

    @Mock
    private SubProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() {
        final var expectedType = SubProviderType.PATREON.getName();
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "https://www.patreon.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var provider = SubProvider.create(
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
        final var expectedErrorMessage = "SubProvider with ID 123 was not found";
        final var expectedId = SubProviderID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway, times(1)).findById(eq(expectedId));
    }
}
