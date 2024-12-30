package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ContentProviderID extends Identifier {

    private final String valor;

    private ContentProviderID(final String valor) {
        this.valor = Objects.requireNonNull(valor, "'ContentProviderID' should not be null");
    }

    public static ContentProviderID unique() {
        return new ContentProviderID(UUID.randomUUID().toString().toLowerCase());
    }

    public static ContentProviderID from (final String id) {
        return new ContentProviderID(id);
    }

    @Override
    public String getValue() {
        return valor;
    }
}
