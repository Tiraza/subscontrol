package br.com.subscontrol.application.sub.create;

import br.com.subscontrol.domain.sub.Sub;

public record CreateSubOutput(String id) {

    public static CreateSubOutput from(final Sub sub) {
        return new CreateSubOutput(sub.getId().getValue());
    }

}
