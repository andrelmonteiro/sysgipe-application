package br.com.sysgipe.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import br.com.sysgipe.domain.Arquivo;
import br.com.sysgipe.repository.ArquivoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Arquivo} entity.
 */
public interface ArquivoSearchRepository extends ElasticsearchRepository<Arquivo, Long>, ArquivoSearchRepositoryInternal {}

interface ArquivoSearchRepositoryInternal {
    Stream<Arquivo> search(String query);

    Stream<Arquivo> search(Query query);

    void index(Arquivo entity);
}

class ArquivoSearchRepositoryInternalImpl implements ArquivoSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ArquivoRepository repository;

    ArquivoSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, ArquivoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Arquivo> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Arquivo> search(Query query) {
        return elasticsearchTemplate.search(query, Arquivo.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Arquivo entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
