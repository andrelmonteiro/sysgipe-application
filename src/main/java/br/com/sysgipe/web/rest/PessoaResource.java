package br.com.sysgipe.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.sysgipe.domain.Pessoa;
import br.com.sysgipe.repository.PessoaRepository;
import br.com.sysgipe.repository.search.PessoaSearchRepository;
import br.com.sysgipe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.sysgipe.domain.Pessoa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PessoaResource {

    private final Logger log = LoggerFactory.getLogger(PessoaResource.class);

    private static final String ENTITY_NAME = "pessoa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PessoaRepository pessoaRepository;

    private final PessoaSearchRepository pessoaSearchRepository;

    public PessoaResource(PessoaRepository pessoaRepository, PessoaSearchRepository pessoaSearchRepository) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaSearchRepository = pessoaSearchRepository;
    }

    /**
     * {@code POST  /pessoas} : Create a new pessoa.
     *
     * @param pessoa the pessoa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pessoa, or with status {@code 400 (Bad Request)} if the pessoa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pessoas")
    public ResponseEntity<Pessoa> createPessoa(@Valid @RequestBody Pessoa pessoa) throws URISyntaxException {
        log.debug("REST request to save Pessoa : {}", pessoa);
        if (pessoa.getId() != null) {
            throw new BadRequestAlertException("A new pessoa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pessoa result = pessoaRepository.save(pessoa);
        pessoaSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/pessoas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pessoas/:id} : Updates an existing pessoa.
     *
     * @param id the id of the pessoa to save.
     * @param pessoa the pessoa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoa,
     * or with status {@code 400 (Bad Request)} if the pessoa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pessoa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> updatePessoa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pessoa pessoa
    ) throws URISyntaxException {
        log.debug("REST request to update Pessoa : {}, {}", id, pessoa);
        if (pessoa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pessoa result = pessoaRepository.save(pessoa);
        pessoaSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pessoas/:id} : Partial updates given fields of an existing pessoa, field will ignore if it is null
     *
     * @param id the id of the pessoa to save.
     * @param pessoa the pessoa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoa,
     * or with status {@code 400 (Bad Request)} if the pessoa is not valid,
     * or with status {@code 404 (Not Found)} if the pessoa is not found,
     * or with status {@code 500 (Internal Server Error)} if the pessoa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pessoas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pessoa> partialUpdatePessoa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pessoa pessoa
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pessoa partially : {}, {}", id, pessoa);
        if (pessoa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pessoa> result = pessoaRepository
            .findById(pessoa.getId())
            .map(existingPessoa -> {
                if (pessoa.getAtivo() != null) {
                    existingPessoa.setAtivo(pessoa.getAtivo());
                }
                if (pessoa.getAutoridade() != null) {
                    existingPessoa.setAutoridade(pessoa.getAutoridade());
                }
                if (pessoa.getContato() != null) {
                    existingPessoa.setContato(pessoa.getContato());
                }

                return existingPessoa;
            })
            .map(pessoaRepository::save)
            .map(savedPessoa -> {
                pessoaSearchRepository.save(savedPessoa);

                return savedPessoa;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoa.getId().toString())
        );
    }

    /**
     * {@code GET  /pessoas} : get all the pessoas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pessoas in body.
     */
    @GetMapping("/pessoas")
    public List<Pessoa> getAllPessoas(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Pessoas");
        if (eagerload) {
            return pessoaRepository.findAllWithEagerRelationships();
        } else {
            return pessoaRepository.findAll();
        }
    }

    /**
     * {@code GET  /pessoas/:id} : get the "id" pessoa.
     *
     * @param id the id of the pessoa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pessoa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> getPessoa(@PathVariable Long id) {
        log.debug("REST request to get Pessoa : {}", id);
        Optional<Pessoa> pessoa = pessoaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(pessoa);
    }

    /**
     * {@code DELETE  /pessoas/:id} : delete the "id" pessoa.
     *
     * @param id the id of the pessoa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pessoas/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
        log.debug("REST request to delete Pessoa : {}", id);
        pessoaRepository.deleteById(id);
        pessoaSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/pessoas?query=:query} : search for the pessoa corresponding
     * to the query.
     *
     * @param query the query of the pessoa search.
     * @return the result of the search.
     */
    @GetMapping("/_search/pessoas")
    public List<Pessoa> searchPessoas(@RequestParam String query) {
        log.debug("REST request to search Pessoas for query {}", query);
        return StreamSupport.stream(pessoaSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
