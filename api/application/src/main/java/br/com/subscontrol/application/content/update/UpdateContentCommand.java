package br.com.subscontrol.application.content.update;

public record UpdateContentCommand(
        String id,
        String label,
        boolean active
) {

    public static UpdateContentCommand with(
            String id,
            String label,
            boolean active) {
        return new UpdateContentCommand(id, label, active);
    }
}
