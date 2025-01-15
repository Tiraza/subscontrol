package br.com.subscontrol.application.provider.content.create;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class CreateContentProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateContentProviderUseCase useCase;

    @Mock
    private ContentProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateWithAuthenticationClientSecret_shouldReturnId() {
        final var expectedType = "Google Drive";
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "http://google.com";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var command = CreateContentProviderCommand.with(
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

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).create(argThat(contentProvider ->
                Objects.equals(expectedType, contentProvider.getType().getName())
                        && Objects.equals(expectedName, contentProvider.getName())
                        && Objects.equals(expectedBaseUrl, contentProvider.getBaseUrl())
                        && Objects.equals(expectedAuthenticationType, contentProvider.getAuthentication().getType())
                        && Objects.equals(expectedClientId, contentProvider.getAuthentication().getClientId())
                        && Objects.equals(expectedClientSecret, contentProvider.getAuthentication().getClientSecret())
                        && Objects.equals(expectedAuthorizationUrl, contentProvider.getAuthentication().getAuthorizationUrl())
                        && Objects.equals(expectedTokenUrl, contentProvider.getAuthentication().getTokenUrl())
        ));
    }

    @Test
    void givenAValidCommand_whenCallsCreateWithAuthenticationClientFile_shouldReturnId() {
        final var expectedType = "Google Drive";
        final var expectedName = "Google Drive Integration";
        final var expectedBaseUrl = "http://google.com";
        final var expectedAuthenticationType = AuthenticationType.FILE;
        final var expectedFile = Base64.getEncoder().encodeToString("test".getBytes());

        final var command = CreateContentProviderCommand.with(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedAuthenticationType.name(),
                null,
                null,
                null,
                null,
                expectedFile
        );

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).create(argThat(contentProvider ->
                Objects.equals(expectedType, contentProvider.getType().getName())
                        && Objects.equals(expectedName, contentProvider.getName())
                        && Objects.equals(expectedBaseUrl, contentProvider.getBaseUrl())
                        && Objects.equals(expectedAuthenticationType, contentProvider.getAuthentication().getType())
                        && !Objects.isNull(contentProvider.getAuthentication().getFile())
        ));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsCreate_thenReceiveDomainException(String errorMessage, String name, String url) {
        final var expectedType = "Google Drive";
        final var expectedAuthenticationType = AuthenticationType.CLIENT_SECRET;
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "/authorization";
        final var expectedTokenUrl = "/token";

        final var command = CreateContentProviderCommand.with(
                expectedType,
                name,
                url,
                expectedAuthenticationType.name(),
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl,
                null
        );

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(0)).create(any());
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
