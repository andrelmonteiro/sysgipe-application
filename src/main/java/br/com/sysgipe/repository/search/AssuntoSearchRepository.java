package br.com.sysgipe.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import br.com.sysgipe.domain.Assunto;
import br.com.sysgipe.repository.AssuntoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Assunto} entity.
 */
public interface AssuntoSearchRepository extends ElasticsearchRepository<Assunto, Long>, AssuntoSearchRepositoryInternal {}

interface AssuntoSearchRepositoryInternal {
    Stream<Assunto> search(String query);

    Stream<Assunto> search(Query query);

    void index(Assunto entity);
}

class AssuntoSearchRepositoryInternalImpl implements AssuntoSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final AssuntoRepository repository;

    AssuntoSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, AssuntoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Assunto> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Assunto> search(Query query) {
        return elasticsearchTemplate.search(query, Assunto.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Assunto entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
