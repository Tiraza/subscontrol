package br.com.subscontrol.application.provider.content.update;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class UpdateContentProviderUseCaseIT {

    @Autowired
    private UpdateContentProviderUseCase useCase;

    @SpyBean
    private ContentProviderGateway gateway;

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAValidCommand_whenCallsUpdate_shouldReturnId() {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE,
                "Google Integration",
                "http://google.com",
                null);


        assertEquals(0, repository.count());

        gateway.create(provider);

        assertEquals(1, repository.count());

        final var expectedId = provider.getId();
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "https://www.google.com";
        final var expectedIsActive = false;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://google.com/authorization";
        final var expectedTokenUrl = "http://google.com/token";

        final var command = UpdateContentProviderCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedBaseUrl,
                expectedIsActive,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var output = useCase.execute(command);

        assertEquals(1, repository.count());

        assertNotNull(output);
        assertNotNull(output.id());

        final var actualProvider = repository.findById(output.id()).get();

        assertEquals(ContentProviderType.GOOGLE_DRIVE.getName(), actualProvider.getType().getName());
        assertEquals(expectedName, actualProvider.getName());
        assertEquals(expectedBaseUrl, actualProvider.getBaseUrl());
        assertEquals(expectedClientId, actualProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, actualProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.getAuthentication().tokenUrl());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = "123";

        final var command = UpdateContentProviderCommand.with(
                expectedId,
                "Google Drive Integration",
                "https://www.google.com",
                true,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "http://google.com/authorization",
                "http://google.com/token"
        );

        assertEquals(0, repository.count());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(0, repository.count());

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("ContentProvider with ID 123 was not found", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsUpdate_thenReceiveDomainException(String errorMessage, String name, String url) {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE,
                "Google Integration",
                "http://google.com",
                null);

        assertEquals(0, repository.count());

        gateway.create(provider);

        assertEquals(1, repository.count());

        final var expectedId = provider.getId();
        final var expectedIsActive = true;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://google.com/authorization";
        final var expectedTokenUrl = "http://google.com/token";

        final var command = UpdateContentProviderCommand.with(
                expectedId.getValue(),
                name,
                url,
                expectedIsActive,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );


        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(1, repository.count());

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());
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
