package br.com.subscontrol.infraestructure.provider.content.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentProviderRepository extends JpaRepository<ContentProviderJpaEntity, String> {}
