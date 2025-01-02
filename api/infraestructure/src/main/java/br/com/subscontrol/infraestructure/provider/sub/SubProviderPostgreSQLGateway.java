package br.com.subscontrol.infraestructure.provider.sub;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.sub.SubProvider;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import br.com.subscontrol.domain.provider.sub.SubProviderID;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import br.com.subscontrol.infraestructure.util.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubProviderPostgreSQLGateway implements SubProviderGateway {

    private final SubProviderRepository repository;

    public SubProviderPostgreSQLGateway(SubProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public SubProvider create(final SubProvider provider) {
        return save(provider);
    }

    @Override
    public void deleteById(final SubProviderID id) {
        final String value = id.getValue();
        if (this.repository.existsById(value)) {
            this.repository.deleteById(value);
        }
    }

    @Override
    public Optional<SubProvider> findById(final SubProviderID id) {
        return this.repository.findById(id.getValue()).map(SubProviderJpaEntity::toDomain);
    }

    @Override
    public SubProvider update(final SubProvider provider) {
        return save(provider);
    }

    @Override
    public Pagination<SubProvider> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var resuls = this.repository.findAll(Specification.where(where), page);

        return new Pagination<>(
                resuls.getNumber(),
                resuls.getSize(),
                resuls.getTotalElements(),
                resuls.map(SubProviderJpaEntity::toDomain).toList()
        );
    }

    private SubProvider save(final SubProvider provider) {
        return this.repository.save(SubProviderJpaEntity.from(provider)).toDomain();
    }

    private Specification<SubProviderJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
