package br.com.subscontrol.application.sub.update;

import br.com.subscontrol.domain.sub.Sub;

public record UpdateSubOutput(String id) {

    public static UpdateSubOutput from(final Sub sub) {
        return new UpdateSubOutput(sub.getId().getValue());
    }

}
