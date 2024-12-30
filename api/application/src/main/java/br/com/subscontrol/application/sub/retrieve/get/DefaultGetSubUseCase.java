package br.com.subscontrol.application.sub.retrieve.get;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.sub.Sub;
import br.com.subscontrol.domain.sub.SubGateway;
import br.com.subscontrol.domain.sub.SubID;

import java.util.Objects;

public class DefaultGetSubUseCase extends GetSubUseCase {

    private final SubGateway gateway;

    public DefaultGetSubUseCase(SubGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public SubOutput execute(final String id) {
        final SubID subID = SubID.from(id);
        return this.gateway.findById(subID)
                .map(SubOutput::from)
                .orElseThrow(NotFoundException.notFound(Sub.class, subID));
    }
}
