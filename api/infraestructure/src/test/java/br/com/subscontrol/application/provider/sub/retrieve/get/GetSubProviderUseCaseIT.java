package br.com.subscontrol.application.provider.sub.retrieve.get;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class GetSubProviderUseCaseIT {

    @Autowired
    private GetSubProviderUseCase useCase;

    @Autowired
    private SubProviderGateway gateway;

    @Autowired
    private SubProviderRepository repository;

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
        assertEquals(expectedClientId, actualProvider.clientId());
        assertEquals(expectedClientSecret, actualProvider.clientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.authorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.tokenUrl());
    }

    @Test
    void givenAValidId_whenCallsGetAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "SubProvider with ID 123 was not found";
        final var expectedId = SubProviderID.from("123");

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
