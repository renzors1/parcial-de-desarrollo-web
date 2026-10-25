package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AutorAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Autor;
import com.mycompany.myapp.repository.AutorRepository;
import com.mycompany.myapp.service.dto.AutorDTO;
import com.mycompany.myapp.service.mapper.AutorMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link AutorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AutorResourceIT {

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_NACIONALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_NACIONALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790047508639L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790047508639L);

    private static final String ENTITY_API_URL = "/api/autors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorMapper autorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutorMockMvc;

    private Autor autor;

    private Autor insertedAutor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autor createEntity() {
        return new Autor()
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .nacionalidad(DEFAULT_NACIONALIDAD)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioModificacion(DEFAULT_USUARIO_MODIFICACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autor createUpdatedEntity() {
        return new Autor()
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        autor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAutor != null) {
            autorRepository.delete(insertedAutor);
            insertedAutor = null;
        }
    }

    @Test
    @Transactional
    void createAutor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);
        var returnedAutorDTO = om.readValue(
            restAutorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AutorDTO.class
        );

        // Validate the Autor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAutor = autorMapper.toEntity(returnedAutorDTO);
        assertAutorUpdatableFieldsEquals(returnedAutor, getPersistedAutor(returnedAutor));

        insertedAutor = returnedAutor;
    }

    @Test
    @Transactional
    void createAutorWithExistingId() throws Exception {
        // Create the Autor with an existing ID
        autor.setId(1L);
        AutorDTO autorDTO = autorMapper.toDto(autor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autor.setNombres(null);

        // Create the Autor, which fails.
        AutorDTO autorDTO = autorMapper.toDto(autor);

        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autor.setApellidos(null);

        // Create the Autor, which fails.
        AutorDTO autorDTO = autorMapper.toDto(autor);

        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAutors() throws Exception {
        // Initialize the database
        insertedAutor = autorRepository.saveAndFlush(autor);

        // Get all the autorList
        restAutorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].nacionalidad").value(hasItem(DEFAULT_NACIONALIDAD)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getAutor() throws Exception {
        // Initialize the database
        insertedAutor = autorRepository.saveAndFlush(autor);

        // Get the autor
        restAutorMockMvc
            .perform(get(ENTITY_API_URL_ID, autor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autor.getId().intValue()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.nacionalidad").value(DEFAULT_NACIONALIDAD))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAutor() throws Exception {
        // Get the autor
        restAutorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAutor() throws Exception {
        // Initialize the database
        insertedAutor = autorRepository.saveAndFlush(autor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autor
        Autor updatedAutor = autorRepository.findById(autor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAutor are not directly saved in db
        em.detach(updatedAutor);
        updatedAutor
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        AutorDTO autorDTO = autorMapper.toDto(updatedAutor);

        restAutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAutorToMatchAllProperties(updatedAutor);
    }

    @Test
    @Transactional
    void putNonExistingAutor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autor.setId(longCount.incrementAndGet());

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autor.setId(longCount.incrementAndGet());

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autor.setId(longCount.incrementAndGet());

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAutorWithPatch() throws Exception {
        // Initialize the database
        insertedAutor = autorRepository.saveAndFlush(autor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autor using partial update
        Autor partialUpdatedAutor = new Autor();
        partialUpdatedAutor.setId(autor.getId());

        partialUpdatedAutor
            .apellidos(UPDATED_APELLIDOS)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutor))
            )
            .andExpect(status().isOk());

        // Validate the Autor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAutor, autor), getPersistedAutor(autor));
    }

    @Test
    @Transactional
    void fullUpdateAutorWithPatch() throws Exception {
        // Initialize the database
        insertedAutor = autorRepository.saveAndFlush(autor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autor using partial update
        Autor partialUpdatedAutor = new Autor();
        partialUpdatedAutor.setId(autor.getId());

        partialUpdatedAutor
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutor))
            )
            .andExpect(status().isOk());

        // Validate the Autor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutorUpdatableFieldsEquals(partialUpdatedAutor, getPersistedAutor(partialUpdatedAutor));
    }

    @Test
    @Transactional
    void patchNonExistingAutor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autor.setId(longCount.incrementAndGet());

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autor.setId(longCount.incrementAndGet());

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autor.setId(longCount.incrementAndGet());

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(autorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAutor() throws Exception {
        // Initialize the database
        insertedAutor = autorRepository.saveAndFlush(autor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the autor
        restAutorMockMvc
            .perform(delete(ENTITY_API_URL_ID, autor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return autorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Autor getPersistedAutor(Autor autor) {
        return autorRepository.findById(autor.getId()).orElseThrow();
    }

    protected void assertPersistedAutorToMatchAllProperties(Autor expectedAutor) {
        assertAutorAllPropertiesEquals(expectedAutor, getPersistedAutor(expectedAutor));
    }

    protected void assertPersistedAutorToMatchUpdatableProperties(Autor expectedAutor) {
        assertAutorAllUpdatablePropertiesEquals(expectedAutor, getPersistedAutor(expectedAutor));
    }
}
