package br.com.subscontrol.domain.content;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

import java.util.Optional;

public interface ContentGateway {

    Content create(Content content);

    void deleteById(ContentID id);

    Optional<Content> findById(ContentID id);

    Content update(Content content);

    Pagination<Content> findAll(SearchQuery query);
}
