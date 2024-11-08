package br.com.subscontrol.domain;

public abstract class ProvidedEntity<ID extends Identifier> extends Entity<ID> {

    protected final String providedId;

    protected ProvidedEntity(ID id, String providedId) {
        super(id);
        this.providedId = providedId;
    }

    public String getProvidedId() {
        return providedId;
    }
}
