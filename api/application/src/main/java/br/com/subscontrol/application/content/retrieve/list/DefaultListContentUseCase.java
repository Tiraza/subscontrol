package br.com.subscontrol.application.content.retrieve.list;

import br.com.subscontrol.domain.content.ContentGateway;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

public class DefaultListContentUseCase extends ListContentUseCase {

    private final ContentGateway gateway;

    public DefaultListContentUseCase(ContentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Pagination<ContentListOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery).map(ContentListOutput::from);
    }
}
