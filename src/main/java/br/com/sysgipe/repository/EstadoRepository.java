package br.com.sysgipe.repository;

import br.com.sysgipe.domain.Estado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Estado entity.
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    default Optional<Estado> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Estado> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Estado> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct estado from Estado estado left join fetch estado.pais",
        countQuery = "select count(distinct estado) from Estado estado"
    )
    Page<Estado> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct estado from Estado estado left join fetch estado.pais")
    List<Estado> findAllWithToOneRelationships();

    @Query("select estado from Estado estado left join fetch estado.pais where estado.id =:id")
    Optional<Estado> findOneWithToOneRelationships(@Param("id") Long id);
}
