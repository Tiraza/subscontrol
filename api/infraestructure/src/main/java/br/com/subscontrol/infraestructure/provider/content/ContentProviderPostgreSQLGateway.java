package br.com.subscontrol.infraestructure.provider.content;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.domain.provider.content.ContentProvider;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import br.com.subscontrol.domain.provider.content.ContentProviderID;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderJpaEntity;
import br.com.subscontrol.infraestructure.provider.content.persistence.ContentProviderRepository;
import br.com.subscontrol.infraestructure.util.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContentProviderPostgreSQLGateway implements ContentProviderGateway {

    private final ContentProviderRepository repository;

    public ContentProviderPostgreSQLGateway(ContentProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContentProvider create(ContentProvider provider) {
        return save(provider);
    }

    @Override
    public void deleteById(ContentProviderID id) {
        final String value = id.getValue();
        if (this.repository.existsById(value)) {
            this.repository.deleteById(value);
        }
    }

    @Override
    public Optional<ContentProvider> findById(ContentProviderID id) {
        return this.repository.findById(id.getValue()).map(ContentProviderJpaEntity::toDomain);
    }

    @Override
    public ContentProvider update(ContentProvider provider) {
        return save(provider);
    }

    @Override
    public Pagination<ContentProvider> findAll(SearchQuery query) {
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
                resuls.map(ContentProviderJpaEntity::toDomain).toList()
        );
    }

    private ContentProvider save(final ContentProvider provider) {
        return this.repository.save(ContentProviderJpaEntity.from(provider)).toDomain();
    }

    private Specification<ContentProviderJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
