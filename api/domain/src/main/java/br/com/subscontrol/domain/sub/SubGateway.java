package br.com.subscontrol.domain.sub;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

import java.util.Optional;

public interface SubGateway {

    Sub create(Sub sub);

    void deleteById(SubID id);

    Optional<Sub> findById(SubID id);

    Sub update(Sub content);

    Pagination<Sub> findAll(SearchQuery query);

}
