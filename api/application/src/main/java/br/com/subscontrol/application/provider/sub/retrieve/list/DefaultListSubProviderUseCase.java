package br.com.subscontrol.application.provider.sub.retrieve.list;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;

import java.util.Objects;

public class DefaultListSubProviderUseCase extends ListSubProviderUseCase {

    private SubProviderGateway gateway;

    public DefaultListSubProviderUseCase(final SubProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<SubProviderListOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery).map(SubProviderListOutput::from);
    }
}
