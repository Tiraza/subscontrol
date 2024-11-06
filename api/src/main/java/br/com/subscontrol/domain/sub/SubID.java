package br.com.subscontrol.domain.sub;

import br.com.subscontrol.domain.Identificador;

import java.util.Objects;
import java.util.UUID;

public class SubID extends Identificador  {

    private final String valor;

    private SubID(final String valor) {
        Objects.requireNonNull(valor);
        this.valor = valor;
    }

    public static SubID unique() {
        return new SubID(UUID.randomUUID().toString().toLowerCase());
    }

    public static SubID from (final String anId) {
        return new SubID(anId);
    }

    @Override
    public String getValor() {
        return valor;
    }
}
