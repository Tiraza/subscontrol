package br.com.subscontrol.application.tier.retrieve.list;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.tier.TierGateway;

import java.util.Objects;

public class DefaultListTierUseCase extends ListTierUseCase {

    private final TierGateway gateway;

    public DefaultListTierUseCase(TierGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<TierListOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery).map(TierListOutput::from);
    }
}
