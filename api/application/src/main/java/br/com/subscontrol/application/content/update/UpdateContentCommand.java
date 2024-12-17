package br.com.subscontrol.application.content.update;

public record UpdateContentCommand(
        String id,
        String providedId,
        String label,
        boolean active
) {

    public static UpdateContentCommand with(
            String id,
            String providedId,
            String label,
            boolean active) {
        return new UpdateContentCommand(id, providedId, label, active);
    }
}
