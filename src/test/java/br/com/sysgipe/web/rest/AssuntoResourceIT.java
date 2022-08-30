package br.com.sysgipe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.sysgipe.IntegrationTest;
import br.com.sysgipe.domain.Assunto;
import br.com.sysgipe.repository.AssuntoRepository;
import br.com.sysgipe.repository.search.AssuntoSearchRepository;
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
 * Integration tests for the {@link AssuntoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssuntoResourceIT {

    private static final String DEFAULT_ARIGO = "AAAAAAAAAA";
    private static final String UPDATED_ARIGO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Integer DEFAULT_CODIGO = 1;
    private static final Integer UPDATED_CODIGO = 2;

    private static final LocalDate DEFAULT_DATA_EXCLUSAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_EXCLUSAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DISPOSITIVO = "AAAAAAAAAA";
    private static final String UPDATED_DISPOSITIVO = "BBBBBBBBBB";

    private static final String DEFAULT_GLOSSARIO = "AAAAAAAAAA";
    private static final String UPDATED_GLOSSARIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAI_ID = 1;
    private static final Integer UPDATED_PAI_ID = 2;

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Integer DEFAULT_USUARIO_EXCLUSAO_ID = 1;
    private static final Integer UPDATED_USUARIO_EXCLUSAO_ID = 2;

    private static final String ENTITY_API_URL = "/api/assuntos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/assuntos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AssuntoSearchRepository assuntoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssuntoMockMvc;

    private Assunto assunto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assunto createEntity(EntityManager em) {
        Assunto assunto = new Assunto()
            .arigo(DEFAULT_ARIGO)
            .ativo(DEFAULT_ATIVO)
            .codigo(DEFAULT_CODIGO)
            .dataExclusao(DEFAULT_DATA_EXCLUSAO)
            .dispositivo(DEFAULT_DISPOSITIVO)
            .glossario(DEFAULT_GLOSSARIO)
            .nome(DEFAULT_NOME)
            .observacao(DEFAULT_OBSERVACAO)
            .paiId(DEFAULT_PAI_ID)
            .tipo(DEFAULT_TIPO)
            .usuarioExclusaoId(DEFAULT_USUARIO_EXCLUSAO_ID);
        return assunto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assunto createUpdatedEntity(EntityManager em) {
        Assunto assunto = new Assunto()
            .arigo(UPDATED_ARIGO)
            .ativo(UPDATED_ATIVO)
            .codigo(UPDATED_CODIGO)
            .dataExclusao(UPDATED_DATA_EXCLUSAO)
            .dispositivo(UPDATED_DISPOSITIVO)
            .glossario(UPDATED_GLOSSARIO)
            .nome(UPDATED_NOME)
            .observacao(UPDATED_OBSERVACAO)
            .paiId(UPDATED_PAI_ID)
            .tipo(UPDATED_TIPO)
            .usuarioExclusaoId(UPDATED_USUARIO_EXCLUSAO_ID);
        return assunto;
    }

    @BeforeEach
    public void initTest() {
        assuntoSearchRepository.deleteAll();
        assunto = createEntity(em);
    }

    @Test
    @Transactional
    void createAssunto() throws Exception {
        int databaseSizeBeforeCreate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        // Create the Assunto
        restAssuntoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isCreated());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getArigo()).isEqualTo(DEFAULT_ARIGO);
        assertThat(testAssunto.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testAssunto.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testAssunto.getDataExclusao()).isEqualTo(DEFAULT_DATA_EXCLUSAO);
        assertThat(testAssunto.getDispositivo()).isEqualTo(DEFAULT_DISPOSITIVO);
        assertThat(testAssunto.getGlossario()).isEqualTo(DEFAULT_GLOSSARIO);
        assertThat(testAssunto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssunto.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testAssunto.getPaiId()).isEqualTo(DEFAULT_PAI_ID);
        assertThat(testAssunto.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testAssunto.getUsuarioExclusaoId()).isEqualTo(DEFAULT_USUARIO_EXCLUSAO_ID);
    }

    @Test
    @Transactional
    void createAssuntoWithExistingId() throws Exception {
        // Create the Assunto with an existing ID
        assunto.setId(1L);

        int databaseSizeBeforeCreate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssuntoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAssuntos() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        // Get all the assuntoList
        restAssuntoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assunto.getId().intValue())))
            .andExpect(jsonPath("$.[*].arigo").value(hasItem(DEFAULT_ARIGO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].dataExclusao").value(hasItem(DEFAULT_DATA_EXCLUSAO.toString())))
            .andExpect(jsonPath("$.[*].dispositivo").value(hasItem(DEFAULT_DISPOSITIVO)))
            .andExpect(jsonPath("$.[*].glossario").value(hasItem(DEFAULT_GLOSSARIO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].paiId").value(hasItem(DEFAULT_PAI_ID)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].usuarioExclusaoId").value(hasItem(DEFAULT_USUARIO_EXCLUSAO_ID)));
    }

    @Test
    @Transactional
    void getAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        // Get the assunto
        restAssuntoMockMvc
            .perform(get(ENTITY_API_URL_ID, assunto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assunto.getId().intValue()))
            .andExpect(jsonPath("$.arigo").value(DEFAULT_ARIGO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.dataExclusao").value(DEFAULT_DATA_EXCLUSAO.toString()))
            .andExpect(jsonPath("$.dispositivo").value(DEFAULT_DISPOSITIVO))
            .andExpect(jsonPath("$.glossario").value(DEFAULT_GLOSSARIO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO))
            .andExpect(jsonPath("$.paiId").value(DEFAULT_PAI_ID))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.usuarioExclusaoId").value(DEFAULT_USUARIO_EXCLUSAO_ID));
    }

    @Test
    @Transactional
    void getNonExistingAssunto() throws Exception {
        // Get the assunto
        restAssuntoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assuntoSearchRepository.save(assunto);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());

        // Update the assunto
        Assunto updatedAssunto = assuntoRepository.findById(assunto.getId()).get();
        // Disconnect from session so that the updates on updatedAssunto are not directly saved in db
        em.detach(updatedAssunto);
        updatedAssunto
            .arigo(UPDATED_ARIGO)
            .ativo(UPDATED_ATIVO)
            .codigo(UPDATED_CODIGO)
            .dataExclusao(UPDATED_DATA_EXCLUSAO)
            .dispositivo(UPDATED_DISPOSITIVO)
            .glossario(UPDATED_GLOSSARIO)
            .nome(UPDATED_NOME)
            .observacao(UPDATED_OBSERVACAO)
            .paiId(UPDATED_PAI_ID)
            .tipo(UPDATED_TIPO)
            .usuarioExclusaoId(UPDATED_USUARIO_EXCLUSAO_ID);

        restAssuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssunto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssunto))
            )
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getArigo()).isEqualTo(UPDATED_ARIGO);
        assertThat(testAssunto.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testAssunto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testAssunto.getDataExclusao()).isEqualTo(UPDATED_DATA_EXCLUSAO);
        assertThat(testAssunto.getDispositivo()).isEqualTo(UPDATED_DISPOSITIVO);
        assertThat(testAssunto.getGlossario()).isEqualTo(UPDATED_GLOSSARIO);
        assertThat(testAssunto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssunto.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAssunto.getPaiId()).isEqualTo(UPDATED_PAI_ID);
        assertThat(testAssunto.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testAssunto.getUsuarioExclusaoId()).isEqualTo(UPDATED_USUARIO_EXCLUSAO_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Assunto> assuntoSearchList = IterableUtils.toList(assuntoSearchRepository.findAll());
                Assunto testAssuntoSearch = assuntoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAssuntoSearch.getArigo()).isEqualTo(UPDATED_ARIGO);
                assertThat(testAssuntoSearch.getAtivo()).isEqualTo(UPDATED_ATIVO);
                assertThat(testAssuntoSearch.getCodigo()).isEqualTo(UPDATED_CODIGO);
                assertThat(testAssuntoSearch.getDataExclusao()).isEqualTo(UPDATED_DATA_EXCLUSAO);
                assertThat(testAssuntoSearch.getDispositivo()).isEqualTo(UPDATED_DISPOSITIVO);
                assertThat(testAssuntoSearch.getGlossario()).isEqualTo(UPDATED_GLOSSARIO);
                assertThat(testAssuntoSearch.getNome()).isEqualTo(UPDATED_NOME);
                assertThat(testAssuntoSearch.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
                assertThat(testAssuntoSearch.getPaiId()).isEqualTo(UPDATED_PAI_ID);
                assertThat(testAssuntoSearch.getTipo()).isEqualTo(UPDATED_TIPO);
                assertThat(testAssuntoSearch.getUsuarioExclusaoId()).isEqualTo(UPDATED_USUARIO_EXCLUSAO_ID);
            });
    }

    @Test
    @Transactional
    void putNonExistingAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assunto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assunto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAssuntoWithPatch() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();

        // Update the assunto using partial update
        Assunto partialUpdatedAssunto = new Assunto();
        partialUpdatedAssunto.setId(assunto.getId());

        partialUpdatedAssunto
            .ativo(UPDATED_ATIVO)
            .codigo(UPDATED_CODIGO)
            .dataExclusao(UPDATED_DATA_EXCLUSAO)
            .glossario(UPDATED_GLOSSARIO)
            .usuarioExclusaoId(UPDATED_USUARIO_EXCLUSAO_ID);

        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssunto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssunto))
            )
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getArigo()).isEqualTo(DEFAULT_ARIGO);
        assertThat(testAssunto.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testAssunto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testAssunto.getDataExclusao()).isEqualTo(UPDATED_DATA_EXCLUSAO);
        assertThat(testAssunto.getDispositivo()).isEqualTo(DEFAULT_DISPOSITIVO);
        assertThat(testAssunto.getGlossario()).isEqualTo(UPDATED_GLOSSARIO);
        assertThat(testAssunto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssunto.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testAssunto.getPaiId()).isEqualTo(DEFAULT_PAI_ID);
        assertThat(testAssunto.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testAssunto.getUsuarioExclusaoId()).isEqualTo(UPDATED_USUARIO_EXCLUSAO_ID);
    }

    @Test
    @Transactional
    void fullUpdateAssuntoWithPatch() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();

        // Update the assunto using partial update
        Assunto partialUpdatedAssunto = new Assunto();
        partialUpdatedAssunto.setId(assunto.getId());

        partialUpdatedAssunto
            .arigo(UPDATED_ARIGO)
            .ativo(UPDATED_ATIVO)
            .codigo(UPDATED_CODIGO)
            .dataExclusao(UPDATED_DATA_EXCLUSAO)
            .dispositivo(UPDATED_DISPOSITIVO)
            .glossario(UPDATED_GLOSSARIO)
            .nome(UPDATED_NOME)
            .observacao(UPDATED_OBSERVACAO)
            .paiId(UPDATED_PAI_ID)
            .tipo(UPDATED_TIPO)
            .usuarioExclusaoId(UPDATED_USUARIO_EXCLUSAO_ID);

        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssunto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssunto))
            )
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getArigo()).isEqualTo(UPDATED_ARIGO);
        assertThat(testAssunto.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testAssunto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testAssunto.getDataExclusao()).isEqualTo(UPDATED_DATA_EXCLUSAO);
        assertThat(testAssunto.getDispositivo()).isEqualTo(UPDATED_DISPOSITIVO);
        assertThat(testAssunto.getGlossario()).isEqualTo(UPDATED_GLOSSARIO);
        assertThat(testAssunto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssunto.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAssunto.getPaiId()).isEqualTo(UPDATED_PAI_ID);
        assertThat(testAssunto.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testAssunto.getUsuarioExclusaoId()).isEqualTo(UPDATED_USUARIO_EXCLUSAO_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assunto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assunto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);
        assuntoRepository.save(assunto);
        assuntoSearchRepository.save(assunto);

        int databaseSizeBeforeDelete = assuntoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assunto
        restAssuntoMockMvc
            .perform(delete(ENTITY_API_URL_ID, assunto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assuntoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAssunto() throws Exception {
        // Initialize the database
        assunto = assuntoRepository.saveAndFlush(assunto);
        assuntoSearchRepository.save(assunto);

        // Search the assunto
        restAssuntoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assunto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assunto.getId().intValue())))
            .andExpect(jsonPath("$.[*].arigo").value(hasItem(DEFAULT_ARIGO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].dataExclusao").value(hasItem(DEFAULT_DATA_EXCLUSAO.toString())))
            .andExpect(jsonPath("$.[*].dispositivo").value(hasItem(DEFAULT_DISPOSITIVO)))
            .andExpect(jsonPath("$.[*].glossario").value(hasItem(DEFAULT_GLOSSARIO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO)))
            .andExpect(jsonPath("$.[*].paiId").value(hasItem(DEFAULT_PAI_ID)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].usuarioExclusaoId").value(hasItem(DEFAULT_USUARIO_EXCLUSAO_ID)));
    }
}
