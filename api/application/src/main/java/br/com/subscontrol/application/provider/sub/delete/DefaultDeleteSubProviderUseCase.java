package br.com.subscontrol.application.provider.sub.delete;

import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;

import java.util.Objects;

public class DefaultDeleteSubProviderUseCase extends DeleteSubProviderUseCase {

    private SubProviderGateway gateway;

    public DefaultDeleteSubProviderUseCase(final SubProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(SubProviderID.from(id));
    }
}
