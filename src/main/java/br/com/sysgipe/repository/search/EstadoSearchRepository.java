package br.com.sysgipe.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import br.com.sysgipe.domain.Estado;
import br.com.sysgipe.repository.EstadoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Estado} entity.
 */
public interface EstadoSearchRepository extends ElasticsearchRepository<Estado, Long>, EstadoSearchRepositoryInternal {}

interface EstadoSearchRepositoryInternal {
    Stream<Estado> search(String query);

    Stream<Estado> search(Query query);

    void index(Estado entity);
}

class EstadoSearchRepositoryInternalImpl implements EstadoSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final EstadoRepository repository;

    EstadoSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, EstadoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Estado> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Estado> search(Query query) {
        return elasticsearchTemplate.search(query, Estado.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Estado entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
