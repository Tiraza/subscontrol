package br.com.subscontrol.application.provider.sub.sync;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubSynchronization;

import java.util.Optional;

public abstract class SubSynchronizer implements SubSynchronization {

    public void synchronize(final SubProvider provider) {
        synchronized (provider.getId().getValue()) {
            Optional.ofNullable(provider.getAuthentication()).ifPresent(this::start);
        }
    }

    private void start(final Authentication authentication) {
        synchronizeTiers(authentication).forEach(tier -> {
            synchronizeSubsFromTier(authentication, tier);
        });
    }
}
