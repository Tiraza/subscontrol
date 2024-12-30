package br.com.subscontrol.application.sub.create;

public record CreateSubCommand(
        String providedId,
        String name,
        String email
) {

    public static CreateSubCommand with(
            String providedId,
            String name,
            String email) {
        return new CreateSubCommand(providedId, name, email);
    }
}
