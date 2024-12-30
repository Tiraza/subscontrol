package br.com.subscontrol.domain.content;

import br.com.subscontrol.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ContentID extends Identifier {

    private final String valor;

    private ContentID(final String valor) {
        this.valor = Objects.requireNonNull(valor, "'ContentID' should not be null");
    }

    public static ContentID unique() {
        return new ContentID(UUID.randomUUID().toString().toLowerCase());
    }

    public static ContentID from (final String anId) {
        return new ContentID(anId);
    }

    @Override
    public String getValue() {
        return valor;
    }
}
