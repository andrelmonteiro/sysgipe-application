package br.com.sysgipe.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import br.com.sysgipe.domain.Municipio;
import br.com.sysgipe.repository.MunicipioRepository;
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
 * Spring Data Elasticsearch repository for the {@link Municipio} entity.
 */
public interface MunicipioSearchRepository extends ElasticsearchRepository<Municipio, Long>, MunicipioSearchRepositoryInternal {}

interface MunicipioSearchRepositoryInternal {
    Stream<Municipio> search(String query);

    Stream<Municipio> search(Query query);

    void index(Municipio entity);
}

class MunicipioSearchRepositoryInternalImpl implements MunicipioSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final MunicipioRepository repository;

    MunicipioSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, MunicipioRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Municipio> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Municipio> search(Query query) {
        return elasticsearchTemplate.search(query, Municipio.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Municipio entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
