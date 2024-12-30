package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class TierID extends Identifier {

    private final String valor;

    private TierID(final String valor) {
        this.valor = Objects.requireNonNull(valor, "'TierID' should not be null");
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

}
