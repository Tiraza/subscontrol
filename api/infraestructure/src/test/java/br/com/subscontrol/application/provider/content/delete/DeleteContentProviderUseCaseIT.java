package br.com.subscontrol.application.provider.content.delete;

import br.com.subscontrol.IntegrationTest;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.domain.provider.content.ContentProviderType;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class DeleteContentProviderUseCaseIT {

    @Autowired
    private DeleteContentProviderUseCase useCase;

    @SpyBean
    private ContentProviderGateway gateway;

    @Autowired
    private ContentProviderRepository repository;

    @Test
    void givenAValidId_whenCallsDelete_shouldDelete() {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Integration",
                "http://google.com",
                "CLIENT_SECRET",
                "123",
                "123",
                "/auth",
                "/token",
                null
        );

        final var expectedId = provider.getId();

        assertEquals(0, repository.count());

        repository.saveAndFlush(ContentProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(0, repository.count());
    }

    @Test
    void givenAnInvalidId_whenCallsDelete_shouldBeOk() {
        final var provider = ContentProvider.create(
                ContentProviderType.GOOGLE_DRIVE.getName(),
                "Google Integration",
                "http://google.com",
                "CLIENT_SECRET",
                "123",
                "123",
                "/auth",
                "/token",
                null
        );

        assertEquals(0, repository.count());

        repository.saveAndFlush(ContentProviderJpaEntity.from(provider));

        assertEquals(1, repository.count());

        final var expectedId = ContentProviderID.from("123");

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        assertEquals(1, repository.count());

    }
}
