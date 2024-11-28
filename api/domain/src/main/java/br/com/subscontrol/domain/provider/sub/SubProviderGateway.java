package br.com.subscontrol.domain.provider.sub;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

import java.util.Optional;

public interface SubProviderGateway {

    SubProvider create(SubProvider provider);

    void deleteById(SubProviderID id);

    Optional<SubProvider> findById(SubProviderID id);

    SubProvider update(SubProvider provider);

    Pagination<SubProvider> findAll(SearchQuery query);
}
