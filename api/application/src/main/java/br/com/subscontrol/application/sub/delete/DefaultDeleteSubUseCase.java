package br.com.subscontrol.application.sub.delete;

import br.com.subscontrol.domain.sub.SubGateway;
import br.com.subscontrol.domain.sub.SubID;

import java.util.Objects;

public class DefaultDeleteSubUseCase extends DeleteSubUseCase {

    private final SubGateway gateway;

    public DefaultDeleteSubUseCase(SubGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(SubID.from(id));
    }
}
