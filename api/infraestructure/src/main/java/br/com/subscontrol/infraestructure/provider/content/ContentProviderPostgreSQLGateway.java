package br.com.subscontrol.infraestructure.provider.content;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;

import java.util.Optional;

public class ContentProviderPostgreSQLGateway implements ContentProviderGateway {

    private final ContentProviderRepository repository;

    public ContentProviderPostgreSQLGateway(ContentProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContentProvider create(ContentProvider provider) {
        return null;
    }

    @Override
    public void deleteById(ContentProviderID id) {

    }

    @Override
    public Optional<ContentProvider> findById(ContentProviderID id) {
        return Optional.empty();
    }

    @Override
    public ContentProvider update(ContentProvider provider) {
        return null;
    }

    @Override
    public Pagination<ContentProvider> findAll(SearchQuery query) {
        return null;
    }
}
