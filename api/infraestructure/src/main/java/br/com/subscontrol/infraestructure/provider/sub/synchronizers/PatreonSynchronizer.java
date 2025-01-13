package br.com.subscontrol.infraestructure.provider.sub.synchronizers;

import br.com.subscontrol.application.provider.sub.sync.SubSynchronizer;
import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.tier.Tier;

import java.util.List;

public class PatreonSynchronizer extends SubSynchronizer {

    @Override
    public List<Tier> synchronizeTiers(Authentication authentication) {
        return List.of();
    }

    @Override
    public void synchronizeSubsFromTier(Authentication authentication, Tier tier) {

    }

}
