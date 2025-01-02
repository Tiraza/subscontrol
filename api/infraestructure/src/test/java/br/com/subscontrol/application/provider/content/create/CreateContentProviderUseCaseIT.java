package br.com.subscontrol.application.provider.content.create;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class CreateContentProviderUseCaseIT {

    @Autowired
    private CreateContentProviderUseCase useCase;

    @Autowired
    private ContentProviderGateway gateway;

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() {
        final var expectedType = "Google Drive";
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "http://google.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://google.com/authorization";
        final var expectedTokenUrl = "http://google.com/token";

        final var command = CreateContentProviderCommand.with(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        assertEquals(0, repository.count());

        final var output = useCase.execute(command);

        assertEquals(1, repository.count());

        assertNotNull(output);
        assertNotNull(output.id());

        final var actualProvider = repository.findById(output.id()).get();

        assertEquals(expectedType, actualProvider.getType().getName());
        assertEquals(expectedName, actualProvider.getName());
        assertEquals(expectedBaseUrl, actualProvider.getBaseUrl());
        assertEquals(expectedClientId, actualProvider.getAuthentication().clientId());
        assertEquals(expectedClientSecret, actualProvider.getAuthentication().clientSecret());
        assertEquals(expectedAuthorizationUrl, actualProvider.getAuthentication().authorizationUrl());
        assertEquals(expectedTokenUrl, actualProvider.getAuthentication().tokenUrl());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsCreate_thenReceiveDomainException(String errorMessage, String name, String url) {
        final var expectedType = "Google Drive";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://google.com/authorization";
        final var expectedTokenUrl = "http://google.com/token";

        final var command = CreateContentProviderCommand.with(
                expectedType,
                name,
                url,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        assertEquals(0, repository.count());

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(0, repository.count());

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
