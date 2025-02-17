package br.com.subscontrol.application.provider.content.retrieve.get;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class GetContentProviderUseCaseIT {

    @Autowired
    private GetContentProviderUseCase useCase;

    @SpyBean
    private ContentProviderGateway gateway;

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAValidId_whenCallsGet_shouldReturnProvider() {
        final var expectedType = ContentProviderType.GOOGLE_DRIVE.getName();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedIsActive = true;
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET.name();
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://google.com/authorization";
        final var expectedTokenUrl = "http://google.com/token";

        final var provider = ContentProvider.create(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        assertEquals(0, repository.count());

        gateway.create(provider);

        assertEquals(1, repository.count());

        final var expectedId = provider.getId();

        final var actualProvider = useCase.execute(expectedId.getValue());

        assertEquals(1, repository.count());

        assertEquals(expectedId.getValue(), actualProvider.id());
        assertEquals(expectedType, actualProvider.type());
        assertEquals(expectedName, actualProvider.name());
        assertEquals(expectedBaseUrl, actualProvider.baseUrl());
        assertEquals(expectedIsActive, actualProvider.isActive());
        assertEquals(expectedAuthenticationType, actualProvider.authenticationType());
        assertEquals(expectedClientId, actualProvider.clientId());
        assertEquals(expectedClientSecret, actualProvider.clientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.authorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.tokenUrl());
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "ContentProvider with ID 123 was not found";
        final var expectedId = ContentProviderID.from("123");

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
