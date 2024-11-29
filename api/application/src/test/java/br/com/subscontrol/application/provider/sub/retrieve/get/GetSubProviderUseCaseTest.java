package br.com.subscontrol.application.provider.sub.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://patreon.com/authorization";
        final var expectedTokenUrl = "http://patreon.com/token";

        final var provider = SubProvider.create(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl);

        final var expectedId = provider.getId();

        when(gateway.findById(any())).thenReturn(Optional.of(provider));

        final var actualProvider = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualProvider.id());
        Assertions.assertEquals(expectedType, actualProvider.type());
        Assertions.assertEquals(expectedName, actualProvider.name());
        Assertions.assertEquals(expectedBaseUrl, actualProvider.baseUrl());
        Assertions.assertEquals(expectedIsActive, actualProvider.isActive());
        Assertions.assertEquals(expectedClientId, actualProvider.clientId());
        Assertions.assertEquals(expectedClientSecret, actualProvider.clientSecret());
        Assertions.assertEquals(expectedAuthorizationUrl, actualProvider.authorizationUrl());
        Assertions.assertEquals(expectedTokenUrl, actualProvider.tokenUrl());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "SubProvider with ID 123 was not found";
        final var expectedId = SubProviderID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }
}
