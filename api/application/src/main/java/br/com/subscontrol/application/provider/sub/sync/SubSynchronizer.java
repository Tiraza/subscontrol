package br.com.subscontrol.application.provider.sub.sync;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubSynchronization;

import java.util.Optional;

public abstract class SubSynchronizer implements SubSynchronization {

    public final void synchronize(final SubProvider provider) {
        synchronized (provider.getId()) {
            Optional.ofNullable(provider.getAuthentication()).ifPresent(this::start);
        }
    }

    protected void start(final Authentication authentication) {
        synchronizeTiers(authentication).forEach(tier -> {
            synchronizeSubsFromTier(authentication, tier);
        });
    }
}
