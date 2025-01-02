package br.com.subscontrol.infraestructure.provider.sub.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubProviderRepository extends JpaRepository<SubProviderJpaEntity, String> {

    Page<SubProviderJpaEntity> findAll(Specification<SubProviderJpaEntity> whereClause, Pageable page);

}
