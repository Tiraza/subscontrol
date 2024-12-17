package br.com.subscontrol.application.content.create;

public record CreateContentCommand(
        String providedId,
        String label
) {

    public static CreateContentCommand with(
            String providedId,
            String label) {
        return new CreateContentCommand(providedId, label);
    }
}
