package br.com.subscontrol.domain.provider.content;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

import java.util.Optional;

public interface ContentProviderGateway {

    ContentProvider create(ContentProvider provider);

    void deleteById(ContentProviderID id);

    Optional<ContentProvider> findById(ContentProviderID id);

    ContentProvider update(ContentProvider provider);

    Pagination<ContentProvider> findAll(SearchQuery query);

}
