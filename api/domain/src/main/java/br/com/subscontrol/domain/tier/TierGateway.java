package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

import java.util.Optional;

public interface TierGateway {

    Tier create(Tier tier);

    void deleteById(TierID id);

    Optional<Tier> findById(TierID id);

    Tier update(Tier tier);

    Pagination<Tier> findAll(SearchQuery query);

}
