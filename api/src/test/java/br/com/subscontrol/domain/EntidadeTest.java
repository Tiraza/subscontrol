package br.com.subscontrol.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntidadeTest {

    private static class EntidadeConcreta extends Entidade<Long> {

        private String valor;

        public EntidadeConcreta(Long id, String valor) {
            super(id);
            this.valor = valor;
        }
    }

    @Test
    void dadoParametrosValidos_quandoChamarNew_deveCriarEntidadeComId() {
        final var id = 1L;
        EntidadeConcreta entidade = new EntidadeConcreta(id, null);
        assertEquals(id, entidade.getId());
    }

    @Test
    void dadoIdNull_quandoChamarNew_deveLancarExececao() {
        final var mensagem = "'id' nao pode ser null";
        final var exception = assertThrows(NullPointerException.class, () -> new EntidadeConcreta(null, null));
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void dadoEntidadesComMesmoId_quandoComparar_entaoDeveRetornarQueSaoIguais() {
        final var entidade1 = new EntidadeConcreta(1L, "Primeiro Valor");
        final var entidade2 = new EntidadeConcreta(1L, "Segundo Valor");
        assertEquals(entidade1, entidade2);
        assertEquals(entidade1.hashCode(), entidade2.hashCode());
    }

    @Test
    void dadoEntidadesComDiferentesIds_quandoComparar_entaoDeveRetornarQueSaoDiferentes() {
        final var entidade1 = new EntidadeConcreta(1L, "Valor");
        final var entidade2 = new EntidadeConcreta(2L, "Valor");
        assertNotEquals(entidade1, entidade2);
        assertNotEquals(entidade1.hashCode(), entidade2.hashCode());
    }
}
