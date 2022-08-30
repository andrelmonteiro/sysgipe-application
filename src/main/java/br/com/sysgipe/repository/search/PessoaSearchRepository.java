package br.com.sysgipe.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import br.com.sysgipe.domain.Pessoa;
import br.com.sysgipe.repository.PessoaRepository;
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
 * Spring Data Elasticsearch repository for the {@link Pessoa} entity.
 */
public interface PessoaSearchRepository extends ElasticsearchRepository<Pessoa, Long>, PessoaSearchRepositoryInternal {}

interface PessoaSearchRepositoryInternal {
    Stream<Pessoa> search(String query);

    Stream<Pessoa> search(Query query);

    void index(Pessoa entity);
}

class PessoaSearchRepositoryInternalImpl implements PessoaSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final PessoaRepository repository;

    PessoaSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, PessoaRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Pessoa> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return search(nativeSearchQuery);
    }

    @Override
    public Stream<Pessoa> search(Query query) {
        return elasticsearchTemplate.search(query, Pessoa.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Pessoa entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
