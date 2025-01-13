package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.provider.ProviderID;

import java.util.UUID;

public class ContentProviderID extends ProviderID {

    private ContentProviderID(final String value) {
        super(value, "ContentProviderID");
    }

    public static ContentProviderID unique() {
        return new ContentProviderID(UUID.randomUUID().toString().toLowerCase());
    }

    public static ContentProviderID from (final String id) {
        return new ContentProviderID(id);
    }

}
