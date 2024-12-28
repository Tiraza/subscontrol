package br.com.subscontrol.application.tier.create;

public record CreateTierCommand(
        String subProviderID,
        String providedId,
        String title,
        String description,
        String amount
) {

    public static CreateTierCommand with(
            String subProviderID,
            String providedId,
            String title,
            String description,
            String amount
    ) {
        return new CreateTierCommand(subProviderID, providedId, title, description, amount);
    }
}
