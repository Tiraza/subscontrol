package br.com.subscontrol.application.provider.sub.retrieve.list;

import br.com.subscontrol.application.UseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

public abstract class ListSubProviderUseCase extends UseCase<SearchQuery, Pagination<SubProviderListOutput>> {

}
