package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.exceptions.NotFoundException;

public enum ContentProviderType {
    GOOGLE_DRIVE("Google Drive");

    private final String name;

    ContentProviderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ContentProviderType from(String name) {
        for (ContentProviderType value : ContentProviderType.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        throw NotFoundException.with(ContentProviderType.class, name);
    }
}
