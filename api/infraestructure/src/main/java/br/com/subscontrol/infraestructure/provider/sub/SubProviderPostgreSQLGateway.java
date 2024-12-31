package br.com.subscontrol.infraestructure.provider.sub;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubProviderPostgreSQLGateway implements SubProviderGateway {

    private final SubProviderRepository repository;

    public SubProviderPostgreSQLGateway(SubProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public SubProvider create(SubProvider provider) {
        return null;
    }

    @Override
    public void deleteById(SubProviderID id) {

    }

    @Override
    public Optional<SubProvider> findById(SubProviderID id) {
        return Optional.empty();
    }

    @Override
    public SubProvider update(SubProvider provider) {
        return null;
    }

    @Override
    public Pagination<SubProvider> findAll(SearchQuery query) {
        return null;
    }
}
