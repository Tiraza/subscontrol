package br.com.subscontrol.application.provider.content.retrieve.list;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;

import java.util.Objects;

public class DefaultListContentProviderUseCase extends ListContentProviderUseCase {

    private final ContentProviderGateway gateway;

    public DefaultListContentProviderUseCase(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ContentProviderListOutput> execute(final SearchQuery searchQuery) {
        return this.gateway.findAll(searchQuery).map(ContentProviderListOutput::from);
    }
}
