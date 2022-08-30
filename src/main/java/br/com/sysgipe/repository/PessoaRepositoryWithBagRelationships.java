package br.com.sysgipe.repository;

import br.com.sysgipe.domain.Pessoa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PessoaRepositoryWithBagRelationships {
    Optional<Pessoa> fetchBagRelationships(Optional<Pessoa> pessoa);

    List<Pessoa> fetchBagRelationships(List<Pessoa> pessoas);

    Page<Pessoa> fetchBagRelationships(Page<Pessoa> pessoas);
}
