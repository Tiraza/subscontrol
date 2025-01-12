package br.com.subscontrol.application.provider.sub.sync;

import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.domain.provider.sub.SubProviderType;

import java.util.Map;
import java.util.Objects;

public class DefaultSyncSubProviderUseCase extends SyncSubProviderUseCase {

    private final SubProviderGateway gateway;
    private final Map<SubProviderType, SubSynchronizer> synchronizers;

    public DefaultSyncSubProviderUseCase(
            final SubProviderGateway gateway,
            final Map<SubProviderType, SubSynchronizer> synchronizers) {
        this.gateway = Objects.requireNonNull(gateway);
        this.synchronizers = Objects.requireNonNull(synchronizers);
    }

    @Override
    public void execute(final String id) {
        final SubProviderID providerID = SubProviderID.from(id);

        final SubProvider subProvider = this.gateway.findById(providerID)
                .orElseThrow(NotFoundException.notFound(SubProvider.class, providerID));

        getSynchronizer(subProvider.getType()).synchronize(subProvider);
    }

    private SubSynchronizer getSynchronizer(final SubProviderType providerType) {
        SubSynchronizer synchronizer = this.synchronizers.get(providerType);
        if (synchronizer == null) {
            throw new IllegalArgumentException("Unknown SubProviderType: " + providerType);
        }
        return synchronizer;
    }
}
