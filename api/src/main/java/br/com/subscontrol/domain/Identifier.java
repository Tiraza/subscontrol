package br.com.subscontrol.domain;

import java.util.Objects;

public abstract class Identifier {

    public abstract String getValor();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(getValor(), that.getValor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValor());
    }
}
