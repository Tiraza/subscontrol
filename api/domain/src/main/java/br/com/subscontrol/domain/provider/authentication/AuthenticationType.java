package br.com.subscontrol.domain.provider.authentication;

import br.com.subscontrol.domain.exceptions.NotFoundException;

public enum AuthenticationType {
    CLIENT_SECRET, FILE;

    public static AuthenticationType from(String name) {
        for (AuthenticationType value : AuthenticationType.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw NotFoundException.with(AuthenticationType.class, name);
    }
}
