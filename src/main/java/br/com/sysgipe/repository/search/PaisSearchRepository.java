package br.com.sysgipe.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import br.com.sysgipe.domain.Pais;
import br.com.sysgipe.repository.PaisRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data Elasticsearch repository for the {@link Pais} entity.
 */
public interface PaisSearchRepository extends ElasticsearchRepository<Pais, Long>, PaisSearchRepositoryInternal {}

interface PaisSearchRepositoryInternal {
    Stream<Pais> search(String query);

    Stream<Pais> search(Query query);

    void index(Pais entity);
}

class PaisSearchRepositoryInternalImpl implements PaisSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final PaisRepository repository;

    PaisSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, PaisRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Pais> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Pais> search(Query query) {
        return elasticsearchTemplate.search(query, Pais.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Pais entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
