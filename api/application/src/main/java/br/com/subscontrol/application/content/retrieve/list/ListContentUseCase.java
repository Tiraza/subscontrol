package br.com.subscontrol.application.content.retrieve.list;

import br.com.subscontrol.application.UseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

public abstract class ListContentUseCase extends UseCase<SearchQuery, Pagination<ContentListOutput>> {
}
