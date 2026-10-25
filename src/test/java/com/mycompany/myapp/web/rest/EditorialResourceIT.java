package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EditorialAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Editorial;
import com.mycompany.myapp.repository.EditorialRepository;
import com.mycompany.myapp.service.dto.EditorialDTO;
import com.mycompany.myapp.service.mapper.EditorialMapper;
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
 * Integration tests for the {@link EditorialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EditorialResourceIT {

    private static final String DEFAULT_NOMBRE_EDITORIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_EDITORIAL = "BBBBBBBBBB";

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790047508639L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790047508639L);

    private static final String ENTITY_API_URL = "/api/editorials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EditorialRepository editorialRepository;

    @Autowired
    private EditorialMapper editorialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEditorialMockMvc;

    private Editorial editorial;

    private Editorial insertedEditorial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Editorial createEntity() {
        return new Editorial()
            .nombreEditorial(DEFAULT_NOMBRE_EDITORIAL)
            .pais(DEFAULT_PAIS)
            .activo(DEFAULT_ACTIVO)
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
    public static Editorial createUpdatedEntity() {
        return new Editorial()
            .nombreEditorial(UPDATED_NOMBRE_EDITORIAL)
            .pais(UPDATED_PAIS)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        editorial = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEditorial != null) {
            editorialRepository.delete(insertedEditorial);
            insertedEditorial = null;
        }
    }

    @Test
    @Transactional
    void createEditorial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);
        var returnedEditorialDTO = om.readValue(
            restEditorialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(editorialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EditorialDTO.class
        );

        // Validate the Editorial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEditorial = editorialMapper.toEntity(returnedEditorialDTO);
        assertEditorialUpdatableFieldsEquals(returnedEditorial, getPersistedEditorial(returnedEditorial));

        insertedEditorial = returnedEditorial;
    }

    @Test
    @Transactional
    void createEditorialWithExistingId() throws Exception {
        // Create the Editorial with an existing ID
        editorial.setId(1L);
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEditorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(editorialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreEditorialIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        editorial.setNombreEditorial(null);

        // Create the Editorial, which fails.
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        restEditorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(editorialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        editorial.setActivo(null);

        // Create the Editorial, which fails.
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        restEditorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(editorialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEditorials() throws Exception {
        // Initialize the database
        insertedEditorial = editorialRepository.saveAndFlush(editorial);

        // Get all the editorialList
        restEditorialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(editorial.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreEditorial").value(hasItem(DEFAULT_NOMBRE_EDITORIAL)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getEditorial() throws Exception {
        // Initialize the database
        insertedEditorial = editorialRepository.saveAndFlush(editorial);

        // Get the editorial
        restEditorialMockMvc
            .perform(get(ENTITY_API_URL_ID, editorial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(editorial.getId().intValue()))
            .andExpect(jsonPath("$.nombreEditorial").value(DEFAULT_NOMBRE_EDITORIAL))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEditorial() throws Exception {
        // Get the editorial
        restEditorialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEditorial() throws Exception {
        // Initialize the database
        insertedEditorial = editorialRepository.saveAndFlush(editorial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the editorial
        Editorial updatedEditorial = editorialRepository.findById(editorial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEditorial are not directly saved in db
        em.detach(updatedEditorial);
        updatedEditorial
            .nombreEditorial(UPDATED_NOMBRE_EDITORIAL)
            .pais(UPDATED_PAIS)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        EditorialDTO editorialDTO = editorialMapper.toDto(updatedEditorial);

        restEditorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, editorialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(editorialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEditorialToMatchAllProperties(updatedEditorial);
    }

    @Test
    @Transactional
    void putNonExistingEditorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        editorial.setId(longCount.incrementAndGet());

        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, editorialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(editorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEditorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        editorial.setId(longCount.incrementAndGet());

        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(editorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEditorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        editorial.setId(longCount.incrementAndGet());

        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(editorialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEditorialWithPatch() throws Exception {
        // Initialize the database
        insertedEditorial = editorialRepository.saveAndFlush(editorial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the editorial using partial update
        Editorial partialUpdatedEditorial = new Editorial();
        partialUpdatedEditorial.setId(editorial.getId());

        partialUpdatedEditorial
            .nombreEditorial(UPDATED_NOMBRE_EDITORIAL)
            .pais(UPDATED_PAIS)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEditorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEditorial))
            )
            .andExpect(status().isOk());

        // Validate the Editorial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEditorialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEditorial, editorial),
            getPersistedEditorial(editorial)
        );
    }

    @Test
    @Transactional
    void fullUpdateEditorialWithPatch() throws Exception {
        // Initialize the database
        insertedEditorial = editorialRepository.saveAndFlush(editorial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the editorial using partial update
        Editorial partialUpdatedEditorial = new Editorial();
        partialUpdatedEditorial.setId(editorial.getId());

        partialUpdatedEditorial
            .nombreEditorial(UPDATED_NOMBRE_EDITORIAL)
            .pais(UPDATED_PAIS)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEditorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEditorial))
            )
            .andExpect(status().isOk());

        // Validate the Editorial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEditorialUpdatableFieldsEquals(partialUpdatedEditorial, getPersistedEditorial(partialUpdatedEditorial));
    }

    @Test
    @Transactional
    void patchNonExistingEditorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        editorial.setId(longCount.incrementAndGet());

        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, editorialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(editorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEditorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        editorial.setId(longCount.incrementAndGet());

        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(editorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEditorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        editorial.setId(longCount.incrementAndGet());

        // Create the Editorial
        EditorialDTO editorialDTO = editorialMapper.toDto(editorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(editorialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Editorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEditorial() throws Exception {
        // Initialize the database
        insertedEditorial = editorialRepository.saveAndFlush(editorial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the editorial
        restEditorialMockMvc
            .perform(delete(ENTITY_API_URL_ID, editorial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return editorialRepository.count();
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

    protected Editorial getPersistedEditorial(Editorial editorial) {
        return editorialRepository.findById(editorial.getId()).orElseThrow();
    }

    protected void assertPersistedEditorialToMatchAllProperties(Editorial expectedEditorial) {
        assertEditorialAllPropertiesEquals(expectedEditorial, getPersistedEditorial(expectedEditorial));
    }

    protected void assertPersistedEditorialToMatchUpdatableProperties(Editorial expectedEditorial) {
        assertEditorialAllUpdatablePropertiesEquals(expectedEditorial, getPersistedEditorial(expectedEditorial));
    }
}
