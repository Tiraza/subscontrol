package br.com.subscontrol.application.content.create;

public record CreateContentCommand(
        String providerID,
        String providedId,
        String label
) {

    public static CreateContentCommand with(
            String providerID,
            String providedId,
            String label) {
        return new CreateContentCommand(providerID, providedId, label);
    }
}
