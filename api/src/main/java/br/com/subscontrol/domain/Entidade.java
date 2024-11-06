package br.com.subscontrol.domain;

import br.com.subscontrol.domain.validation.ValidationHandler;

import java.util.Objects;

public abstract class Entidade<ID> {

    protected final ID id;

    public Entidade(ID id) {
        Objects.requireNonNull(id, "'id' nao pode ser null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entidade<?> entity = (Entidade<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
