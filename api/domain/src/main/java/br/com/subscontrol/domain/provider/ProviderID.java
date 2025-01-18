package br.com.subscontrol.domain.provider;

import br.com.subscontrol.domain.Identifier;

import java.util.Objects;

public abstract class ProviderID extends Identifier {

    protected final String value;

    protected ProviderID(final String value, final String providerIDName) {
        this.value = Objects.requireNonNull(value, "'%s' should not be null".formatted(providerIDName));
    }

    @Override
    public String getValue() {
        return value;
    }
}
