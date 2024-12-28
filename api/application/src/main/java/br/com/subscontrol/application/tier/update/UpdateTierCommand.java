package br.com.subscontrol.application.tier.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record UpdateTierCommand(
        String id,
        String providedId,
        String title,
        String description,
        String amount,
        List<String> subTiers,
        List<String> subscribers,
        List<String> contents,
        boolean active
) {

    public static UpdateTierCommand with(
            String id,
            String providedId,
            String title,
            String description,
            String amount,
            List<String> subTiers,
            List<String> subscribers,
            List<String> contents,
            boolean active
    ) {
        return new UpdateTierCommand(
                id,
                providedId,
                title,
                description,
                amount,
                new ArrayList<>(subTiers != null ? subTiers : Collections.emptyList()),
                new ArrayList<>(subscribers != null ? subscribers : Collections.emptyList()),
                new ArrayList<>(contents != null ? contents : Collections.emptyList()),
                active
        );
    }
}
