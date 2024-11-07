package br.com.subscontrol.domain;

import br.com.subscontrol.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private static class EntityConcreta extends Entity<Long> {

        private String valor;

        public EntityConcreta(Long id, String valor) {
            super(id);
            this.valor = valor;
        }

        @Override
        public void validate(ValidationHandler handler) {
            /* Entidade criada apenas para teste */
        }
    }

    @Test
    void dadoParametrosValidos_quandoChamarNew_deveCriarEntidadeComId() {
        final var id = 1L;
        EntityConcreta entidade = new EntityConcreta(id, null);
        assertEquals(id, entidade.getId());
    }

    @Test
    void dadoIdNull_quandoChamarNew_deveLancarExececao() {
        final var mensagem = "'id' should not be null";
        final var exception = assertThrows(NullPointerException.class, () -> new EntityConcreta(null, null));
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void dadoEntidadesComMesmoId_quandoComparar_entaoDeveRetornarQueSaoIguais() {
        final var entidade1 = new EntityConcreta(1L, "Primeiro Valor");
        final var entidade2 = new EntityConcreta(1L, "Segundo Valor");
        assertEquals(entidade1, entidade2);
        assertEquals(entidade1.hashCode(), entidade2.hashCode());
    }

    @Test
    void dadoEntidadesComDiferentesIds_quandoComparar_entaoDeveRetornarQueSaoDiferentes() {
        final var entidade1 = new EntityConcreta(1L, "Valor");
        final var entidade2 = new EntityConcreta(2L, "Valor");
        assertNotEquals(entidade1, entidade2);
        assertNotEquals(entidade1.hashCode(), entidade2.hashCode());
    }
}
