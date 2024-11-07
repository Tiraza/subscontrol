package br.com.subscontrol.domain.sub;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.utils.ThreadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SubTest {

    @Test
    void givenValidParameters_whenCallNewSub_thenInstantiateASub() {
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.tiraza@subscontrol.com.br";

        Sub sub = Sub.newSub(name, email);

        assertNotNull(sub.getId());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertTrue(sub.isActive());
        assertNotNull(sub.getCreatedAt());
        assertNotNull(sub.getUpdatedAt());
        assertNull(sub.getDeletedAt());
    }

    @Test
    void givenValidParameters_whenCallWith_thenInstantiateASub() {
        final var id = SubID.unique().getValor();
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.tiraza@subscontrol.com.br";
        final var isActive = false;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Sub sub = Sub.with(id, name, email, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, sub.getId().getValor());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertEquals(isActive, sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertEquals(updatedAt, sub.getUpdatedAt());
        assertEquals(deletedAt, sub.getDeletedAt());
    }

    @Test
    void givenActiveSub_whenCallDeactivate_thenDeactivateASub() {
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.tiraza@subscontrol.com.br";

        Sub sub = Sub.newSub(name, email);

        assertNotNull(sub.getId());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertTrue(sub.isActive());
        assertNotNull(sub.getCreatedAt());
        assertNotNull(sub.getUpdatedAt());
        assertNull(sub.getDeletedAt());

        final var id = sub.getId();
        final var createdAt = sub.getCreatedAt();
        final var updatedAt = sub.getUpdatedAt();

        ThreadUtils.sleep();
        sub.deactivate();

        assertEquals(id, sub.getId());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertFalse(sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertTrue(updatedAt.isBefore(sub.getUpdatedAt()));
        assertNotNull(sub.getDeletedAt());
    }

    @Test
    void givenInactiveSub_whenCallActivate_thenActivateASub() {
        final var id = SubID.unique().toString();
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.tiraza@subscontrol.com.br";
        final var isActive = false;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Sub sub = Sub.with(id, name, email, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, sub.getId().getValor());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertEquals(isActive, sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertEquals(updatedAt, sub.getUpdatedAt());
        assertEquals(deletedAt, sub.getDeletedAt());

        ThreadUtils.sleep();
        sub.activate();

        assertEquals(id, sub.getId().getValor());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertTrue(sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertTrue(updatedAt.isBefore(sub.getUpdatedAt()));
        assertNull(sub.getDeletedAt());
    }

    @Test
    void givenAValidInactiveSub_whenCallUpdateWithActivate_shouldReceiveSubUpdated() {
        final var id = SubID.unique().getValor();
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.tiraza@subscontrol.com.br";
        final var isActive = false;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();
        final var deletedAt = InstantUtils.now();

        Sub sub = Sub.with(id, name, email, isActive, createdAt, updatedAt, deletedAt);

        assertEquals(id, sub.getId().getValor());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertEquals(isActive, sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertEquals(updatedAt, sub.getUpdatedAt());
        assertEquals(deletedAt, sub.getDeletedAt());

        ThreadUtils.sleep();
        final var expectedName = "Victor Tiraza Santos";
        final var expectedEmail = "victor.tiraza@subscontrol.com.br";
        sub.update(expectedName, expectedEmail, true);

        assertEquals(id, sub.getId().getValor());
        assertEquals(expectedName, sub.getName());
        assertEquals(expectedEmail, sub.getEmail());
        assertTrue(sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertTrue(updatedAt.isBefore(sub.getUpdatedAt()));
        assertNull(sub.getDeletedAt());
    }

    @Test
    void givenAValidActiveSub_whenCallUpdateWithInactivate_shouldReceiveSubUpdated() {
        final var id = SubID.unique().getValor();
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.tiraza@subscontrol.com.br";
        final var isActive = true;
        final var createdAt = InstantUtils.now();
        final var updatedAt = InstantUtils.now();

        Sub sub = Sub.with(id, name, email, isActive, createdAt, updatedAt, null);

        assertEquals(id, sub.getId().getValor());
        assertEquals(name, sub.getName());
        assertEquals(email, sub.getEmail());
        assertEquals(isActive, sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertEquals(updatedAt, sub.getUpdatedAt());
        assertNull(sub.getDeletedAt());

        ThreadUtils.sleep();
        final var expectedName = "Victor Tiraza Santos";
        final var expectedEmail = "victor.tiraza@subscontrol.com.br";
        sub.update(expectedName, expectedEmail, false);

        assertEquals(id, sub.getId().getValor());
        assertEquals(expectedName, sub.getName());
        assertEquals(expectedEmail, sub.getEmail());
        assertFalse(sub.isActive());
        assertEquals(createdAt, sub.getCreatedAt());
        assertTrue(updatedAt.isBefore(sub.getUpdatedAt()));
        assertNotNull(sub.getDeletedAt());
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    void givenInvalidParameter_whenCallNewSub_thenReceiveDomainException(String errorMessage, String name, String email) {
        DomainException exception = assertThrows(DomainException.class, () -> Sub.newSub(name, email));

        final var expectedMessage = "Failed to create Entity Sub";
        final int expectedErrorCount = 1;

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.firstError().message());
    }

    private static Stream<Arguments> provideArguments() {
        final var name = "Muryllo Tiraza Santos";
        final var email = "muryllo.santos@subscontrol.com.br";
        final var invalidLengthValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum bibendum eros vel orci finibus, et tincidunt velit placerat. Suspendisse potenti. Maecenas consequat lorem sit amet diam venenatis, non auctor odio posuere. Nulla facilisi. Sed vulputate eros nec nisl maximus, in efficitur velit ultricies. Phasellus vitae turpis risus. Nam a libero ex. Etiam venenatis pharetra diam, in hendrerit libero fermentum a. Integer sit amet lacinia turpis. Sed eget tortor fringilla, posuere elit a, fermentum odio.";

        return Stream.of(
                Arguments.of("'name' should not be null", null, email),
                Arguments.of("'name' should not be empty", "  ", email),
                Arguments.of("'name' must be between 1 and 255 characters", invalidLengthValue, email),
                Arguments.of("'email' should not be null", name, null),
                Arguments.of("'email' should not be empty", name, "  "),
                Arguments.of("'email' must be between 1 and 255 characters", name, invalidLengthValue)
        );
    }
}
