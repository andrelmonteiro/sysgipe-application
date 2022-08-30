package br.com.sysgipe.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import br.com.sysgipe.domain.Arquivo;
import br.com.sysgipe.repository.ArquivoRepository;
import br.com.sysgipe.repository.search.ArquivoSearchRepository;
import br.com.sysgipe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.sysgipe.domain.Arquivo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArquivoResource {

    private final Logger log = LoggerFactory.getLogger(ArquivoResource.class);

    private static final String ENTITY_NAME = "arquivo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArquivoRepository arquivoRepository;

    private final ArquivoSearchRepository arquivoSearchRepository;

    public ArquivoResource(ArquivoRepository arquivoRepository, ArquivoSearchRepository arquivoSearchRepository) {
        this.arquivoRepository = arquivoRepository;
        this.arquivoSearchRepository = arquivoSearchRepository;
    }

    /**
     * {@code POST  /arquivos} : Create a new arquivo.
     *
     * @param arquivo the arquivo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arquivo, or with status {@code 400 (Bad Request)} if the arquivo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arquivos")
    public ResponseEntity<Arquivo> createArquivo(@RequestBody Arquivo arquivo) throws URISyntaxException {
        log.debug("REST request to save Arquivo : {}", arquivo);
        if (arquivo.getId() != null) {
            throw new BadRequestAlertException("A new arquivo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Arquivo result = arquivoRepository.save(arquivo);
        arquivoSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/arquivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arquivos/:id} : Updates an existing arquivo.
     *
     * @param id the id of the arquivo to save.
     * @param arquivo the arquivo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivo,
     * or with status {@code 400 (Bad Request)} if the arquivo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arquivo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arquivos/{id}")
    public ResponseEntity<Arquivo> updateArquivo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Arquivo arquivo)
        throws URISyntaxException {
        log.debug("REST request to update Arquivo : {}, {}", id, arquivo);
        if (arquivo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Arquivo result = arquivoRepository.save(arquivo);
        arquivoSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /arquivos/:id} : Partial updates given fields of an existing arquivo, field will ignore if it is null
     *
     * @param id the id of the arquivo to save.
     * @param arquivo the arquivo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arquivo,
     * or with status {@code 400 (Bad Request)} if the arquivo is not valid,
     * or with status {@code 404 (Not Found)} if the arquivo is not found,
     * or with status {@code 500 (Internal Server Error)} if the arquivo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arquivos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Arquivo> partialUpdateArquivo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Arquivo arquivo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Arquivo partially : {}, {}", id, arquivo);
        if (arquivo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arquivo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arquivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Arquivo> result = arquivoRepository
            .findById(arquivo.getId())
            .map(existingArquivo -> {
                if (arquivo.getAssinado() != null) {
                    existingArquivo.setAssinado(arquivo.getAssinado());
                }
                if (arquivo.getAtivo() != null) {
                    existingArquivo.setAtivo(arquivo.getAtivo());
                }
                if (arquivo.getDateCreated() != null) {
                    existingArquivo.setDateCreated(arquivo.getDateCreated());
                }
                if (arquivo.getDiretorio() != null) {
                    existingArquivo.setDiretorio(arquivo.getDiretorio());
                }
                if (arquivo.getDocumentoId() != null) {
                    existingArquivo.setDocumentoId(arquivo.getDocumentoId());
                }
                if (arquivo.getHash() != null) {
                    existingArquivo.setHash(arquivo.getHash());
                }
                if (arquivo.getHashConteudo() != null) {
                    existingArquivo.setHashConteudo(arquivo.getHashConteudo());
                }
                if (arquivo.getHistorico() != null) {
                    existingArquivo.setHistorico(arquivo.getHistorico());
                }
                if (arquivo.getLacunaToken() != null) {
                    existingArquivo.setLacunaToken(arquivo.getLacunaToken());
                }
                if (arquivo.getLastUpdate() != null) {
                    existingArquivo.setLastUpdate(arquivo.getLastUpdate());
                }
                if (arquivo.getMimeType() != null) {
                    existingArquivo.setMimeType(arquivo.getMimeType());
                }
                if (arquivo.getNome() != null) {
                    existingArquivo.setNome(arquivo.getNome());
                }

                return existingArquivo;
            })
            .map(arquivoRepository::save)
            .map(savedArquivo -> {
                arquivoSearchRepository.save(savedArquivo);

                return savedArquivo;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arquivo.getId().toString())
        );
    }

    /**
     * {@code GET  /arquivos} : get all the arquivos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arquivos in body.
     */
    @GetMapping("/arquivos")
    public List<Arquivo> getAllArquivos() {
        log.debug("REST request to get all Arquivos");
        return arquivoRepository.findAll();
    }

    /**
     * {@code GET  /arquivos/:id} : get the "id" arquivo.
     *
     * @param id the id of the arquivo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arquivo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arquivos/{id}")
    public ResponseEntity<Arquivo> getArquivo(@PathVariable Long id) {
        log.debug("REST request to get Arquivo : {}", id);
        Optional<Arquivo> arquivo = arquivoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(arquivo);
    }

    /**
     * {@code DELETE  /arquivos/:id} : delete the "id" arquivo.
     *
     * @param id the id of the arquivo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arquivos/{id}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable Long id) {
        log.debug("REST request to delete Arquivo : {}", id);
        arquivoRepository.deleteById(id);
        arquivoSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/arquivos?query=:query} : search for the arquivo corresponding
     * to the query.
     *
     * @param query the query of the arquivo search.
     * @return the result of the search.
     */
    @GetMapping("/_search/arquivos")
    public List<Arquivo> searchArquivos(@RequestParam String query) {
        log.debug("REST request to search Arquivos for query {}", query);
        return StreamSupport.stream(arquivoSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
