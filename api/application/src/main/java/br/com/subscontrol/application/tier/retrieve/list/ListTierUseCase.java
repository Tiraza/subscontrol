package br.com.subscontrol.application.tier.retrieve.list;

import br.com.subscontrol.application.UseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;

public abstract class ListTierUseCase extends UseCase<SearchQuery, Pagination<TierListOutput>> {
}
