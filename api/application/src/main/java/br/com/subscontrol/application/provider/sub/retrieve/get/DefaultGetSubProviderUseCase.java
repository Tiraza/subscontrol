package br.com.subscontrol.application.provider.sub.retrieve.get;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;

import java.util.Objects;

public class DefaultGetSubProviderUseCase extends GetSubProviderUseCase {

    private SubProviderGateway gateway;

    public DefaultGetSubProviderUseCase(final SubProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public SubProviderOutPut execute(final String id) {
        final SubProviderID providerID = SubProviderID.from(id);
        return this.gateway.findById(providerID)
                .map(SubProviderOutPut::from)
                .orElseThrow(NotFoundException.notFound(SubProvider.class, providerID));
    }
}
