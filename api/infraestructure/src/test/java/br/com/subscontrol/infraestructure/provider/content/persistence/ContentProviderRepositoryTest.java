package br.com.subscontrol.infraestructure.provider.content.persistence;

import br.com.subscontrol.PostgreSQLGatewayTest;
import br.com.subscontrol.domain.provider.authentication.AuthenticationType;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PostgreSQLGatewayTest
class ContentProviderRepositoryTest {

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderJpaEntity.name";

        final var entity = ContentProviderJpaEntity.from(getProviderInstance());
        entity.setName(null);

        final var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var cause = assertInstanceOf(PropertyValueException.class, exception.getCause());

        assertEquals(expectedPropertyName, cause.getPropertyName());
        assertEquals(expectedMessage, cause.getMessage());
    }

    @Test
    void givenAnInvalidNullType_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "type";
        final var expectedMessage = "not-null property references a null or transient value : br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderJpaEntity.type";

        final var entity = ContentProviderJpaEntity.from(getProviderInstance());
        entity.setType(null);

        final var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var cause = assertInstanceOf(PropertyValueException.class, exception.getCause());

        assertEquals(expectedPropertyName, cause.getPropertyName());
        assertEquals(expectedMessage, cause.getMessage());
    }

    private ContentProvider getProviderInstance() {
        return ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Drive Integration",
                "https://www.google.com",
                AuthenticationType.CLIENT_SECRET.name(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "/authorization",
                "/token",
                null
        );
    }

}
