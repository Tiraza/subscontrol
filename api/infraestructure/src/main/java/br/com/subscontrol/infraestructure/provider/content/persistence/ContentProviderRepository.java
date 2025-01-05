package br.com.subscontrol.infraestructure.provider.content.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentProviderRepository extends JpaRepository<ContentProviderJpaEntity, String> {

    Page<ContentProviderJpaEntity> findAll(Specification<ContentProviderJpaEntity> whereClause, Pageable page);

}
