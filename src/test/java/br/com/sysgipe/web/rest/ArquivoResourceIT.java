package br.com.sysgipe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.sysgipe.IntegrationTest;
import br.com.sysgipe.domain.Arquivo;
import br.com.sysgipe.repository.ArquivoRepository;
import br.com.sysgipe.repository.search.ArquivoSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArquivoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArquivoResourceIT {

    private static final String DEFAULT_ASSINADO = "AAAAAAAAAA";
    private static final String UPDATED_ASSINADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATIVO = 1;
    private static final Integer UPDATED_ATIVO = 2;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DIRETORIO = "AAAAAAAAAA";
    private static final String UPDATED_DIRETORIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOCUMENTO_ID = 1;
    private static final Integer UPDATED_DOCUMENTO_ID = 2;

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_HASH_CONTEUDO = "AAAAAAAAAA";
    private static final String UPDATED_HASH_CONTEUDO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HISTORICO = false;
    private static final Boolean UPDATED_HISTORICO = true;

    private static final String DEFAULT_LACUNA_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_LACUNA_TOKEN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/arquivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/arquivos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private ArquivoSearchRepository arquivoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArquivoMockMvc;

    private Arquivo arquivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arquivo createEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .assinado(DEFAULT_ASSINADO)
            .ativo(DEFAULT_ATIVO)
            .dateCreated(DEFAULT_DATE_CREATED)
            .diretorio(DEFAULT_DIRETORIO)
            .documentoId(DEFAULT_DOCUMENTO_ID)
            .hash(DEFAULT_HASH)
            .hashConteudo(DEFAULT_HASH_CONTEUDO)
            .historico(DEFAULT_HISTORICO)
            .lacunaToken(DEFAULT_LACUNA_TOKEN)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .mimeType(DEFAULT_MIME_TYPE)
            .nome(DEFAULT_NOME);
        return arquivo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arquivo createUpdatedEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .assinado(UPDATED_ASSINADO)
            .ativo(UPDATED_ATIVO)
            .dateCreated(UPDATED_DATE_CREATED)
            .diretorio(UPDATED_DIRETORIO)
            .documentoId(UPDATED_DOCUMENTO_ID)
            .hash(UPDATED_HASH)
            .hashConteudo(UPDATED_HASH_CONTEUDO)
            .historico(UPDATED_HISTORICO)
            .lacunaToken(UPDATED_LACUNA_TOKEN)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .mimeType(UPDATED_MIME_TYPE)
            .nome(UPDATED_NOME);
        return arquivo;
    }

    @BeforeEach
    public void initTest() {
        arquivoSearchRepository.deleteAll();
        arquivo = createEntity(em);
    }

    @Test
    @Transactional
    void createArquivo() throws Exception {
        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        // Create the Arquivo
        restArquivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivo)))
            .andExpect(status().isCreated());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getAssinado()).isEqualTo(DEFAULT_ASSINADO);
        assertThat(testArquivo.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testArquivo.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testArquivo.getDiretorio()).isEqualTo(DEFAULT_DIRETORIO);
        assertThat(testArquivo.getDocumentoId()).isEqualTo(DEFAULT_DOCUMENTO_ID);
        assertThat(testArquivo.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testArquivo.getHashConteudo()).isEqualTo(DEFAULT_HASH_CONTEUDO);
        assertThat(testArquivo.getHistorico()).isEqualTo(DEFAULT_HISTORICO);
        assertThat(testArquivo.getLacunaToken()).isEqualTo(DEFAULT_LACUNA_TOKEN);
        assertThat(testArquivo.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testArquivo.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testArquivo.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createArquivoWithExistingId() throws Exception {
        // Create the Arquivo with an existing ID
        arquivo.setId(1L);

        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivo)))
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllArquivos() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].assinado").value(hasItem(DEFAULT_ASSINADO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].diretorio").value(hasItem(DEFAULT_DIRETORIO)))
            .andExpect(jsonPath("$.[*].documentoId").value(hasItem(DEFAULT_DOCUMENTO_ID)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].hashConteudo").value(hasItem(DEFAULT_HASH_CONTEUDO)))
            .andExpect(jsonPath("$.[*].historico").value(hasItem(DEFAULT_HISTORICO.booleanValue())))
            .andExpect(jsonPath("$.[*].lacunaToken").value(hasItem(DEFAULT_LACUNA_TOKEN)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get the arquivo
        restArquivoMockMvc
            .perform(get(ENTITY_API_URL_ID, arquivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arquivo.getId().intValue()))
            .andExpect(jsonPath("$.assinado").value(DEFAULT_ASSINADO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.diretorio").value(DEFAULT_DIRETORIO))
            .andExpect(jsonPath("$.documentoId").value(DEFAULT_DOCUMENTO_ID))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.hashConteudo").value(DEFAULT_HASH_CONTEUDO))
            .andExpect(jsonPath("$.historico").value(DEFAULT_HISTORICO.booleanValue()))
            .andExpect(jsonPath("$.lacunaToken").value(DEFAULT_LACUNA_TOKEN))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingArquivo() throws Exception {
        // Get the arquivo
        restArquivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        arquivoSearchRepository.save(arquivo);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());

        // Update the arquivo
        Arquivo updatedArquivo = arquivoRepository.findById(arquivo.getId()).get();
        // Disconnect from session so that the updates on updatedArquivo are not directly saved in db
        em.detach(updatedArquivo);
        updatedArquivo
            .assinado(UPDATED_ASSINADO)
            .ativo(UPDATED_ATIVO)
            .dateCreated(UPDATED_DATE_CREATED)
            .diretorio(UPDATED_DIRETORIO)
            .documentoId(UPDATED_DOCUMENTO_ID)
            .hash(UPDATED_HASH)
            .hashConteudo(UPDATED_HASH_CONTEUDO)
            .historico(UPDATED_HISTORICO)
            .lacunaToken(UPDATED_LACUNA_TOKEN)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .mimeType(UPDATED_MIME_TYPE)
            .nome(UPDATED_NOME);

        restArquivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArquivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArquivo))
            )
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getAssinado()).isEqualTo(UPDATED_ASSINADO);
        assertThat(testArquivo.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testArquivo.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testArquivo.getDiretorio()).isEqualTo(UPDATED_DIRETORIO);
        assertThat(testArquivo.getDocumentoId()).isEqualTo(UPDATED_DOCUMENTO_ID);
        assertThat(testArquivo.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testArquivo.getHashConteudo()).isEqualTo(UPDATED_HASH_CONTEUDO);
        assertThat(testArquivo.getHistorico()).isEqualTo(UPDATED_HISTORICO);
        assertThat(testArquivo.getLacunaToken()).isEqualTo(UPDATED_LACUNA_TOKEN);
        assertThat(testArquivo.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testArquivo.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Arquivo> arquivoSearchList = IterableUtils.toList(arquivoSearchRepository.findAll());
                Arquivo testArquivoSearch = arquivoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testArquivoSearch.getAssinado()).isEqualTo(UPDATED_ASSINADO);
                assertThat(testArquivoSearch.getAtivo()).isEqualTo(UPDATED_ATIVO);
                assertThat(testArquivoSearch.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
                assertThat(testArquivoSearch.getDiretorio()).isEqualTo(UPDATED_DIRETORIO);
                assertThat(testArquivoSearch.getDocumentoId()).isEqualTo(UPDATED_DOCUMENTO_ID);
                assertThat(testArquivoSearch.getHash()).isEqualTo(UPDATED_HASH);
                assertThat(testArquivoSearch.getHashConteudo()).isEqualTo(UPDATED_HASH_CONTEUDO);
                assertThat(testArquivoSearch.getHistorico()).isEqualTo(UPDATED_HISTORICO);
                assertThat(testArquivoSearch.getLacunaToken()).isEqualTo(UPDATED_LACUNA_TOKEN);
                assertThat(testArquivoSearch.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
                assertThat(testArquivoSearch.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
                assertThat(testArquivoSearch.getNome()).isEqualTo(UPDATED_NOME);
            });
    }

    @Test
    @Transactional
    void putNonExistingArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        arquivo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        arquivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arquivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        arquivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arquivo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateArquivoWithPatch() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo using partial update
        Arquivo partialUpdatedArquivo = new Arquivo();
        partialUpdatedArquivo.setId(arquivo.getId());

        partialUpdatedArquivo
            .assinado(UPDATED_ASSINADO)
            .dateCreated(UPDATED_DATE_CREATED)
            .diretorio(UPDATED_DIRETORIO)
            .hashConteudo(UPDATED_HASH_CONTEUDO)
            .historico(UPDATED_HISTORICO)
            .mimeType(UPDATED_MIME_TYPE)
            .nome(UPDATED_NOME);

        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArquivo))
            )
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getAssinado()).isEqualTo(UPDATED_ASSINADO);
        assertThat(testArquivo.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testArquivo.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testArquivo.getDiretorio()).isEqualTo(UPDATED_DIRETORIO);
        assertThat(testArquivo.getDocumentoId()).isEqualTo(DEFAULT_DOCUMENTO_ID);
        assertThat(testArquivo.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testArquivo.getHashConteudo()).isEqualTo(UPDATED_HASH_CONTEUDO);
        assertThat(testArquivo.getHistorico()).isEqualTo(UPDATED_HISTORICO);
        assertThat(testArquivo.getLacunaToken()).isEqualTo(DEFAULT_LACUNA_TOKEN);
        assertThat(testArquivo.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testArquivo.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateArquivoWithPatch() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo using partial update
        Arquivo partialUpdatedArquivo = new Arquivo();
        partialUpdatedArquivo.setId(arquivo.getId());

        partialUpdatedArquivo
            .assinado(UPDATED_ASSINADO)
            .ativo(UPDATED_ATIVO)
            .dateCreated(UPDATED_DATE_CREATED)
            .diretorio(UPDATED_DIRETORIO)
            .documentoId(UPDATED_DOCUMENTO_ID)
            .hash(UPDATED_HASH)
            .hashConteudo(UPDATED_HASH_CONTEUDO)
            .historico(UPDATED_HISTORICO)
            .lacunaToken(UPDATED_LACUNA_TOKEN)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .mimeType(UPDATED_MIME_TYPE)
            .nome(UPDATED_NOME);

        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArquivo))
            )
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getAssinado()).isEqualTo(UPDATED_ASSINADO);
        assertThat(testArquivo.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testArquivo.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testArquivo.getDiretorio()).isEqualTo(UPDATED_DIRETORIO);
        assertThat(testArquivo.getDocumentoId()).isEqualTo(UPDATED_DOCUMENTO_ID);
        assertThat(testArquivo.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testArquivo.getHashConteudo()).isEqualTo(UPDATED_HASH_CONTEUDO);
        assertThat(testArquivo.getHistorico()).isEqualTo(UPDATED_HISTORICO);
        assertThat(testArquivo.getLacunaToken()).isEqualTo(UPDATED_LACUNA_TOKEN);
        assertThat(testArquivo.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testArquivo.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        arquivo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arquivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        arquivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arquivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        arquivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(arquivo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);
        arquivoRepository.save(arquivo);
        arquivoSearchRepository.save(arquivo);

        int databaseSizeBeforeDelete = arquivoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the arquivo
        restArquivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, arquivo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(arquivoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchArquivo() throws Exception {
        // Initialize the database
        arquivo = arquivoRepository.saveAndFlush(arquivo);
        arquivoSearchRepository.save(arquivo);

        // Search the arquivo
        restArquivoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + arquivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].assinado").value(hasItem(DEFAULT_ASSINADO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].diretorio").value(hasItem(DEFAULT_DIRETORIO)))
            .andExpect(jsonPath("$.[*].documentoId").value(hasItem(DEFAULT_DOCUMENTO_ID)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].hashConteudo").value(hasItem(DEFAULT_HASH_CONTEUDO)))
            .andExpect(jsonPath("$.[*].historico").value(hasItem(DEFAULT_HISTORICO.booleanValue())))
            .andExpect(jsonPath("$.[*].lacunaToken").value(hasItem(DEFAULT_LACUNA_TOKEN)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }
}
