package br.com.subscontrol.application.provider.sub.delete;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class DeleteSubProviderUseCaseIT {

    @Autowired
    private DeleteSubProviderUseCase useCase;

    @SpyBean
    private SubProviderGateway gateway;

    @Autowired
    private SubProviderRepository repository;

    @Test
    void givenAValidId_whenCallsDelete_shouldDelete() {
        final var provider = SubProvider.create(
                "Patreon",
                "Patreon Integration",
                "http://patreon.com",
                "CLIENT_SECRET",
                "123",
                "123",
                "/auth",
                "/token",
                null
        );

        final var expectedId = provider.getId();

        assertEquals(0, repository.count());

        repository.saveAndFlush(SubProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(0, repository.count());
    }

    @Test
    void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var provider = SubProvider.create(
                "Patreon",
                "Patreon Integration",
                "http://patreon.com",
                "CLIENT_SECRET",
                "123",
                "123",
                "/auth",
                "/token",
                null
        );

        assertEquals(0, repository.count());

        repository.saveAndFlush(SubProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        final var expectedId = SubProviderID.from("123");

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(1, repository.count());
    }

}
