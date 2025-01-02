package br.com.subscontrol.infraestructure.provider.sub.persistence;

import br.com.subscontrol.PostgreSQLGatewayTest;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderType;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@PostgreSQLGatewayTest
class SubProviderRepositoryTest {

    @Autowired
    private SubProviderRepository repository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderJpaEntity.name";

        final var entity = SubProviderJpaEntity.from(getProviderInstance());
        entity.setName(null);

        final var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var cause = assertInstanceOf(PropertyValueException.class, exception.getCause());

        assertEquals(expectedPropertyName, cause.getPropertyName());
        assertEquals(expectedMessage, cause.getMessage());
    }

    @Test
    void givenAnInvalidNullType_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "type";
        final var expectedMessage = "not-null property references a null or transient value : br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderJpaEntity.type";

        final var entity = SubProviderJpaEntity.from(getProviderInstance());
        entity.setType(null);

        final var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var cause = assertInstanceOf(PropertyValueException.class, exception.getCause());

        assertEquals(expectedPropertyName, cause.getPropertyName());
        assertEquals(expectedMessage, cause.getMessage());
    }

    private SubProvider getProviderInstance() {
        return SubProvider.create(
                SubProviderType.PATREON.getName(),
                "Patreon Integration",
                "https://www.patreon.com",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "http://patreon.com/authorization",
                "http://patreon.com/token");
    }

}
