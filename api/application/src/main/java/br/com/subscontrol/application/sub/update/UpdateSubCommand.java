package br.com.subscontrol.application.sub.update;

public record UpdateSubCommand(
        String id,
        String name,
        String email,
        String providedId,
        boolean active
) {

    public static UpdateSubCommand with(
            String id,
            String name,
            String email,
            String providedId,
            boolean active) {
        return new UpdateSubCommand(id, name, email, providedId, active);
    }
}
