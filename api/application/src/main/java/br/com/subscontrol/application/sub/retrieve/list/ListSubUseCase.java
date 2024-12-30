package br.com.subscontrol.application.sub.retrieve.list;

import br.com.subscontrol.application.UseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

public abstract class ListSubUseCase extends UseCase<SearchQuery, Pagination<SubListOutput>> {
}
