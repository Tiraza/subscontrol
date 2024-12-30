package br.com.subscontrol.application.provider.content.update;

import br.com.subscontrol.application.UseCaseTest;
import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class UpdateContentProviderUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateContentProviderUseCase useCase;

    @Mock
    private ContentProviderGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdate_shouldReturnId() {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE,
                "Google Integration",
                "http://google.com",
                null);

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

        when(gateway.findById(any())).thenReturn(Optional.of(provider));

        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.id());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(1)).update(argThat(contentProvider ->
                Objects.equals(expectedName, contentProvider.getName())
                        && Objects.equals(expectedBaseUrl, contentProvider.getBaseUrl())
                        && Objects.equals(expectedIsActive, contentProvider.isActive())
                        && Objects.equals(expectedClientId, contentProvider.getAuthentication().clientId())
                        && Objects.equals(expectedClientSecret, contentProvider.getAuthentication().clientSecret())
                        && Objects.equals(expectedAuthorizationUrl, contentProvider.getAuthentication().authorizationUrl())
                        && Objects.equals(expectedTokenUrl, contentProvider.getAuthentication().tokenUrl())
        ));
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

        when(gateway.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("ContentProvider with ID 123 was not found", exception.getMessage());

        Mockito.verify(gateway, times(1)).findById(eq(ContentProviderID.from(expectedId)));

        Mockito.verify(gateway, times(0)).update(any());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidCommand_whenCallsUpdate_thenReceiveDomainException(String errorMessage, String name, String url) {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE,
                "Google Integration",
                "http://google.com",
                null);

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

        when(gateway.findById(any())).thenReturn(Optional.of(provider));

        final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(command));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(1, exception.getErrors().size());
        Assertions.assertEquals(errorMessage, exception.firstError().message());

        Mockito.verify(gateway, times(1)).findById(eq(expectedId));

        Mockito.verify(gateway, times(0)).update(any());
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
