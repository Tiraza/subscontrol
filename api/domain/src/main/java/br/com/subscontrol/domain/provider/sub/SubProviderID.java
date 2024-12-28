package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class SubProviderID extends Identifier  {

    private final String valor;

    private SubProviderID(final String valor) {
        this.valor = Objects.requireNonNull(valor, "'SubProviderID' should not be null");;
    }

    public static SubProviderID unique() {
        return new SubProviderID(UUID.randomUUID().toString().toLowerCase());
    }

    public static SubProviderID from (final String anId) {
        return new SubProviderID(anId);
    }

    @Override
    public String getValue() {
        return valor;
    }
}
