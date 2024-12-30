package br.com.subscontrol.application.sub.update;

public record UpdateSubCommand(
        String id,
        String name,
        String email,
        boolean isActive
) {

    public static UpdateSubCommand with(
            String id,
            String name,
            String email,
            boolean isActive) {
        return new UpdateSubCommand(id, name, email, isActive);
    }
}
