package br.com.subscontrol.application.provider.sub.create;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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

class CreateSubProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateSubProviderUseCase useCase;

    @Mock
    private SubProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreate_shouldReturnId() {
        final var expectedType = "Patreon";
        final var expectedName = "Patreon Integration";
        final var expectedBaseUrl = "http://patreon.com";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://patreon.com/authorization";
        final var expectedTokenUrl = "http://patreon.com/token";

        final var command = CreateSubProviderCommand.with(
                expectedType,
                expectedName,
                expectedBaseUrl,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).create(argThat(subProvider ->
                Objects.equals(expectedType, subProvider.getType().getName())
                        && Objects.equals(expectedName, subProvider.getName())
                        && Objects.equals(expectedBaseUrl, subProvider.getBaseUrl())
                        && Objects.equals(expectedClientId, subProvider.getAuthentication().clientId())
                        && Objects.equals(expectedClientSecret, subProvider.getAuthentication().clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, subProvider.getAuthentication().authorizationUrl())
                        && Objects.equals(expectedTokenUrl, subProvider.getAuthentication().tokenUrl())
        ));
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsCreate_thenReceiveDomainException(String errorMessage, String name, String url) {
        final var expectedType = "Patreon";
        final var expectedClientId = UUID.randomUUID().toString();
        final var expectedClientSecret = UUID.randomUUID().toString();
        final var expectedAuthorizationUrl = "http://patreon.com/authorization";
        final var expectedTokenUrl = "http://patreon.com/token";

        final var command = CreateSubProviderCommand.with(
                expectedType,
                name,
                url,
                expectedClientId,
                expectedClientSecret,
                expectedAuthorizationUrl,
                expectedTokenUrl
        );

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(0)).create(any());
    }

    private static Stream<Arguments> provideArguments() {
        final var name = "Patreon Integration";
        final var validUrl = "http://www.patreon.com";
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
