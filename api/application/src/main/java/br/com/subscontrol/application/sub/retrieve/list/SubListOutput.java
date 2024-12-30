package br.com.subscontrol.application.sub.retrieve.list;

import br.com.subscontrol.domain.sub.Sub;

public record SubListOutput(
        String id,
        String name,
        String email,
        boolean active
) {

    public static SubListOutput from(final Sub sub) {
        return new SubListOutput(
                sub.getId().getValue(),
                sub.getName(),
                sub.getEmail(),
                sub.isActive()
        );
    }
}
