package br.com.sysgipe.repository;

import br.com.sysgipe.domain.Pessoa;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PessoaRepositoryWithBagRelationshipsImpl implements PessoaRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Pessoa> fetchBagRelationships(Optional<Pessoa> pessoa) {
        return pessoa.map(this::fetchEnderecos);
    }

    @Override
    public Page<Pessoa> fetchBagRelationships(Page<Pessoa> pessoas) {
        return new PageImpl<>(fetchBagRelationships(pessoas.getContent()), pessoas.getPageable(), pessoas.getTotalElements());
    }

    @Override
    public List<Pessoa> fetchBagRelationships(List<Pessoa> pessoas) {
        return Optional.of(pessoas).map(this::fetchEnderecos).orElse(Collections.emptyList());
    }

    Pessoa fetchEnderecos(Pessoa result) {
        return entityManager
            .createQuery("select pessoa from Pessoa pessoa left join fetch pessoa.enderecos where pessoa is :pessoa", Pessoa.class)
            .setParameter("pessoa", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Pessoa> fetchEnderecos(List<Pessoa> pessoas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pessoas.size()).forEach(index -> order.put(pessoas.get(index).getId(), index));
        List<Pessoa> result = entityManager
            .createQuery(
                "select distinct pessoa from Pessoa pessoa left join fetch pessoa.enderecos where pessoa in :pessoas",
                Pessoa.class
            )
            .setParameter("pessoas", pessoas)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
