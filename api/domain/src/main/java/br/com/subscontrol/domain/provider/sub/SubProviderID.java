package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.provider.ProviderID;

import java.util.UUID;

public class SubProviderID extends ProviderID {

    private SubProviderID(final String value) {
        super(value, "SubProviderID");
    }

    public static SubProviderID unique() {
        return new SubProviderID(UUID.randomUUID().toString().toLowerCase());
    }

    public static SubProviderID from (final String anId) {
        return new SubProviderID(anId);
    }

}
