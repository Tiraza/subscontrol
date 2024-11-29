package br.com.subscontrol.application.provider.content.retrieve.list;

import br.com.subscontrol.application.UseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

public abstract class ListContentProviderUseCase extends UseCase<SearchQuery, Pagination<ContentProviderListOutput>> {

}
