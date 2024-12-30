package br.com.subscontrol.domain;

import br.com.subscontrol.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EntityTest {

    @Test
    void givenValidId_whenInstantiating_thenShouldCreateEntity() {
        final StubID id = new StubID();
        final StubEntity entity = new StubEntity(id);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
    }

    @Test
    void givenNullId_whenInstantiating_thenShouldThrowException() {
        final StubID id = null;

        Assertions.assertThrows(
                NullPointerException.class,
                () -> new StubEntity(id),
                "'id' should not be null"
        );
    }

    @Test
    void givenTwoEntitiesWithSameId_whenComparingEquality_thenShouldBeEqual() {
        final StubID id = new StubID();
        final StubEntity entity1 = new StubEntity(id);
        final StubEntity entity2 = new StubEntity(id);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void givenTwoEntitiesWithDifferentIds_whenComparingEquality_thenShouldNotBeEqual() {
        final StubEntity entity1 = new StubEntity(new StubID());
        final StubEntity entity2 = new StubEntity(new StubID());

        Assertions.assertNotEquals(entity1, entity2);
        Assertions.assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void givenEntityAndDifferentClass_whenComparingEquality_thenShouldNotBeEqual() {
        final StubID id = new StubID();
        final StubEntity entity = new StubEntity(id);
        final DifferentEntity differentEntity = new DifferentEntity(id);
        Assertions.assertNotEquals(entity, differentEntity);
    }

    private static class StubID extends Identifier {

        private final String value;

        public StubID() {
            value = UUID.randomUUID().toString();
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static class StubEntity extends Entity<StubID> {
        public StubEntity(StubID id) {
            super(id);
        }

        @Override
        public void validate(ValidationHandler handler) {
            // Not needed for these tests
        }
    }

    private static class DifferentEntity extends Entity<StubID> {
        public DifferentEntity(StubID id) {
            super(id);
        }

        @Override
        public void validate(ValidationHandler handler) {
            // Not needed for these tests
        }
    }
}
