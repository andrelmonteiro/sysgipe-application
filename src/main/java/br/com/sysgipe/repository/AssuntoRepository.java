package br.com.sysgipe.repository;

import br.com.sysgipe.domain.Assunto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Assunto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {}
