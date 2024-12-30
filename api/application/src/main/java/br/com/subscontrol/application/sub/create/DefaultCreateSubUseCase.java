package br.com.subscontrol.application.sub.create;

import br.com.subscontrol.domain.sub.Sub;
import br.com.subscontrol.domain.sub.SubGateway;

import java.util.Objects;

public class DefaultCreateSubUseCase extends CreateSubUseCase {

    private final SubGateway gateway;

    public DefaultCreateSubUseCase(SubGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateSubOutput execute(final CreateSubCommand command) {
        Sub sub = Sub.create(command.providedId(), command.name(), command.email());
        return CreateSubOutput.from(this.gateway.create(sub));
    }

}
