package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.provider.authentication.Authentication;
import br.com.subscontrol.domain.tier.Tier;

import java.util.List;

public interface SubSynchronization {

    List<Tier> synchronizeTiers(final Authentication authentication);

    void synchronizeSubsFromTier(final Authentication authentication, final Tier tier);

}
