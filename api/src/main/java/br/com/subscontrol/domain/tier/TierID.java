package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class TierID extends Identifier {

    private final String valor;

    private TierID(final String valor) {
        Objects.requireNonNull(valor);
        this.valor = valor;
    }

    public static TierID unique() {
        return new TierID(UUID.randomUUID().toString().toLowerCase());
    }

    public static TierID from (final String anId) {
        return new TierID(anId);
    }

    @Override
    public String getValue() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TierID identifier = (TierID) o;
        return Objects.equals(getValue(), identifier.getValue());
    }
}
