package br.com.subscontrol.application.provider.content.retrieve.get;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
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
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://google.com/authorization";
        final var expectedTokenUrl = "http://google.com/token";

        final var provider = ContentProvider.create(
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
        final var expectedErrorMessage = "ContentProvider with ID 123 was not found";
        final var expectedId = ContentProviderID.from("123");

        when(gateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));
    }
}
