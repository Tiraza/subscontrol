package br.com.subscontrol.application.sub.update;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.sub.Sub;
import br.com.subscontrol.domain.sub.SubGateway;
import br.com.subscontrol.domain.sub.SubID;

public class DefaultUpdateSubUseCase extends UpdateSubUseCase {

    private final SubGateway gateway;

    public DefaultUpdateSubUseCase(SubGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public UpdateSubOutput execute(final UpdateSubCommand command) {
        SubID subID = SubID.from(command.id());
        Sub sub = this.gateway.findById(subID).orElseThrow(NotFoundException.notFound(Sub.class, subID));
        sub.update(
                command.name(),
                command.email(),
                command.providedId(),
                command.active()
        );
        return UpdateSubOutput.from(this.gateway.update(sub));
    }

}
