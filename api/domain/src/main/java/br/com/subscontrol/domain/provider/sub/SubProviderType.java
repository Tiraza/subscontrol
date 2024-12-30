package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.exceptions.NotFoundException;

public enum SubProviderType {
    PATREON("Patreon");

    private final String name;

    SubProviderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SubProviderType from(String name) {
        for (SubProviderType value : SubProviderType.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw NotFoundException.with(SubProviderType.class, name);
    }
}
