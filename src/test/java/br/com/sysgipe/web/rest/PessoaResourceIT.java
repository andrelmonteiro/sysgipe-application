package br.com.sysgipe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.sysgipe.IntegrationTest;
import br.com.sysgipe.domain.Pessoa;
import br.com.sysgipe.repository.PessoaRepository;
import br.com.sysgipe.repository.search.PessoaSearchRepository;
import java.util.ArrayList;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PessoaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PessoaResourceIT {

    private static final Integer DEFAULT_ATIVO = 1;
    private static final Integer UPDATED_ATIVO = 2;

    private static final Boolean DEFAULT_AUTORIDADE = false;
    private static final Boolean UPDATED_AUTORIDADE = true;

    private static final String DEFAULT_CONTATO = "AAAAAAAAAA";
    private static final String UPDATED_CONTATO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pessoas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/pessoas";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaRepository pessoaRepositoryMock;

    @Autowired
    private PessoaSearchRepository pessoaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPessoaMockMvc;

    private Pessoa pessoa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa().ativo(DEFAULT_ATIVO).autoridade(DEFAULT_AUTORIDADE).contato(DEFAULT_CONTATO);
        return pessoa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createUpdatedEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa().ativo(UPDATED_ATIVO).autoridade(UPDATED_AUTORIDADE).contato(UPDATED_CONTATO);
        return pessoa;
    }

    @BeforeEach
    public void initTest() {
        pessoaSearchRepository.deleteAll();
        pessoa = createEntity(em);
    }

    @Test
    @Transactional
    void createPessoa() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        // Create the Pessoa
        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isCreated());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testPessoa.getAutoridade()).isEqualTo(DEFAULT_AUTORIDADE);
        assertThat(testPessoa.getContato()).isEqualTo(DEFAULT_CONTATO);
    }

    @Test
    @Transactional
    void createPessoaWithExistingId() throws Exception {
        // Create the Pessoa with an existing ID
        pessoa.setId(1L);

        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkContatoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        // set the field null
        pessoa.setContato(null);

        // Create the Pessoa, which fails.

        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPessoas() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].autoridade").value(hasItem(DEFAULT_AUTORIDADE.booleanValue())))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPessoasWithEagerRelationshipsIsEnabled() throws Exception {
        when(pessoaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPessoaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pessoaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPessoasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pessoaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPessoaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pessoaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get the pessoa
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL_ID, pessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pessoa.getId().intValue()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.autoridade").value(DEFAULT_AUTORIDADE.booleanValue()))
            .andExpect(jsonPath("$.contato").value(DEFAULT_CONTATO));
    }

    @Test
    @Transactional
    void getNonExistingPessoa() throws Exception {
        // Get the pessoa
        restPessoaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoaSearchRepository.save(pessoa);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());

        // Update the pessoa
        Pessoa updatedPessoa = pessoaRepository.findById(pessoa.getId()).get();
        // Disconnect from session so that the updates on updatedPessoa are not directly saved in db
        em.detach(updatedPessoa);
        updatedPessoa.ativo(UPDATED_ATIVO).autoridade(UPDATED_AUTORIDADE).contato(UPDATED_CONTATO);

        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPessoa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPessoa.getAutoridade()).isEqualTo(UPDATED_AUTORIDADE);
        assertThat(testPessoa.getContato()).isEqualTo(UPDATED_CONTATO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Pessoa> pessoaSearchList = IterableUtils.toList(pessoaSearchRepository.findAll());
                Pessoa testPessoaSearch = pessoaSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPessoaSearch.getAtivo()).isEqualTo(UPDATED_ATIVO);
                assertThat(testPessoaSearch.getAutoridade()).isEqualTo(UPDATED_AUTORIDADE);
                assertThat(testPessoaSearch.getContato()).isEqualTo(UPDATED_CONTATO);
            });
    }

    @Test
    @Transactional
    void putNonExistingPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        pessoa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        pessoa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        pessoa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePessoaWithPatch() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa using partial update
        Pessoa partialUpdatedPessoa = new Pessoa();
        partialUpdatedPessoa.setId(pessoa.getId());

        partialUpdatedPessoa.ativo(UPDATED_ATIVO).autoridade(UPDATED_AUTORIDADE);

        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPessoa.getAutoridade()).isEqualTo(UPDATED_AUTORIDADE);
        assertThat(testPessoa.getContato()).isEqualTo(DEFAULT_CONTATO);
    }

    @Test
    @Transactional
    void fullUpdatePessoaWithPatch() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa using partial update
        Pessoa partialUpdatedPessoa = new Pessoa();
        partialUpdatedPessoa.setId(pessoa.getId());

        partialUpdatedPessoa.ativo(UPDATED_ATIVO).autoridade(UPDATED_AUTORIDADE).contato(UPDATED_CONTATO);

        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPessoa.getAutoridade()).isEqualTo(UPDATED_AUTORIDADE);
        assertThat(testPessoa.getContato()).isEqualTo(UPDATED_CONTATO);
    }

    @Test
    @Transactional
    void patchNonExistingPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        pessoa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        pessoa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pessoa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        pessoa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pessoa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        pessoaRepository.save(pessoa);
        pessoaSearchRepository.save(pessoa);

        int databaseSizeBeforeDelete = pessoaRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the pessoa
        restPessoaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pessoa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(pessoaSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPessoa() throws Exception {
        // Initialize the database
        pessoa = pessoaRepository.saveAndFlush(pessoa);
        pessoaSearchRepository.save(pessoa);

        // Search the pessoa
        restPessoaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + pessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].autoridade").value(hasItem(DEFAULT_AUTORIDADE.booleanValue())))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)));
    }
}
