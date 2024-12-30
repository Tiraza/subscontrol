package br.com.subscontrol.application.sub.retrieve.list;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.sub.SubGateway;

import java.util.Objects;

public class DefaultListSubUseCase extends ListSubUseCase {

    private final SubGateway gateway;

    public DefaultListSubUseCase(SubGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }


    @Override
    public Pagination<SubListOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery).map(SubListOutput::from);
    }
}
