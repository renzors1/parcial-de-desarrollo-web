package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LicenciaDigitalAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LicenciaDigital;
import com.mycompany.myapp.domain.enumeration.EstadoLicencia;
import com.mycompany.myapp.repository.LicenciaDigitalRepository;
import com.mycompany.myapp.service.dto.LicenciaDigitalDTO;
import com.mycompany.myapp.service.mapper.LicenciaDigitalMapper;
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
 * Integration tests for the {@link LicenciaDigitalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LicenciaDigitalResourceIT {

    private static final String DEFAULT_CODIGO_LICENCIA = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_LICENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_ENLACE_DESCARGA = "AAAAAAAAAA";
    private static final String UPDATED_ENLACE_DESCARGA = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_ASIGNACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ASIGNACION = Instant.ofEpochMilli(1790047508639L);

    private static final EstadoLicencia DEFAULT_ESTADO = EstadoLicencia.DISPONIBLE;
    private static final EstadoLicencia UPDATED_ESTADO = EstadoLicencia.VENDIDA;

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790047508639L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790047508639L);

    private static final String ENTITY_API_URL = "/api/licencia-digitals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LicenciaDigitalRepository licenciaDigitalRepository;

    @Autowired
    private LicenciaDigitalMapper licenciaDigitalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLicenciaDigitalMockMvc;

    private LicenciaDigital licenciaDigital;

    private LicenciaDigital insertedLicenciaDigital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LicenciaDigital createEntity() {
        return new LicenciaDigital()
            .codigoLicencia(DEFAULT_CODIGO_LICENCIA)
            .enlaceDescarga(DEFAULT_ENLACE_DESCARGA)
            .fechaAsignacion(DEFAULT_FECHA_ASIGNACION)
            .estado(DEFAULT_ESTADO)
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
    public static LicenciaDigital createUpdatedEntity() {
        return new LicenciaDigital()
            .codigoLicencia(UPDATED_CODIGO_LICENCIA)
            .enlaceDescarga(UPDATED_ENLACE_DESCARGA)
            .fechaAsignacion(UPDATED_FECHA_ASIGNACION)
            .estado(UPDATED_ESTADO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        licenciaDigital = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLicenciaDigital != null) {
            licenciaDigitalRepository.delete(insertedLicenciaDigital);
            insertedLicenciaDigital = null;
        }
    }

    @Test
    @Transactional
    void createLicenciaDigital() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);
        var returnedLicenciaDigitalDTO = om.readValue(
            restLicenciaDigitalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(licenciaDigitalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LicenciaDigitalDTO.class
        );

        // Validate the LicenciaDigital in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLicenciaDigital = licenciaDigitalMapper.toEntity(returnedLicenciaDigitalDTO);
        assertLicenciaDigitalUpdatableFieldsEquals(returnedLicenciaDigital, getPersistedLicenciaDigital(returnedLicenciaDigital));

        insertedLicenciaDigital = returnedLicenciaDigital;
    }

    @Test
    @Transactional
    void createLicenciaDigitalWithExistingId() throws Exception {
        // Create the LicenciaDigital with an existing ID
        licenciaDigital.setId(1L);
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicenciaDigitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(licenciaDigitalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoLicenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        licenciaDigital.setCodigoLicencia(null);

        // Create the LicenciaDigital, which fails.
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        restLicenciaDigitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(licenciaDigitalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        licenciaDigital.setEstado(null);

        // Create the LicenciaDigital, which fails.
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        restLicenciaDigitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(licenciaDigitalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLicenciaDigitals() throws Exception {
        // Initialize the database
        insertedLicenciaDigital = licenciaDigitalRepository.saveAndFlush(licenciaDigital);

        // Get all the licenciaDigitalList
        restLicenciaDigitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenciaDigital.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoLicencia").value(hasItem(DEFAULT_CODIGO_LICENCIA)))
            .andExpect(jsonPath("$.[*].enlaceDescarga").value(hasItem(DEFAULT_ENLACE_DESCARGA)))
            .andExpect(jsonPath("$.[*].fechaAsignacion").value(hasItem(DEFAULT_FECHA_ASIGNACION.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getLicenciaDigital() throws Exception {
        // Initialize the database
        insertedLicenciaDigital = licenciaDigitalRepository.saveAndFlush(licenciaDigital);

        // Get the licenciaDigital
        restLicenciaDigitalMockMvc
            .perform(get(ENTITY_API_URL_ID, licenciaDigital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(licenciaDigital.getId().intValue()))
            .andExpect(jsonPath("$.codigoLicencia").value(DEFAULT_CODIGO_LICENCIA))
            .andExpect(jsonPath("$.enlaceDescarga").value(DEFAULT_ENLACE_DESCARGA))
            .andExpect(jsonPath("$.fechaAsignacion").value(DEFAULT_FECHA_ASIGNACION.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLicenciaDigital() throws Exception {
        // Get the licenciaDigital
        restLicenciaDigitalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLicenciaDigital() throws Exception {
        // Initialize the database
        insertedLicenciaDigital = licenciaDigitalRepository.saveAndFlush(licenciaDigital);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the licenciaDigital
        LicenciaDigital updatedLicenciaDigital = licenciaDigitalRepository.findById(licenciaDigital.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLicenciaDigital are not directly saved in db
        em.detach(updatedLicenciaDigital);
        updatedLicenciaDigital
            .codigoLicencia(UPDATED_CODIGO_LICENCIA)
            .enlaceDescarga(UPDATED_ENLACE_DESCARGA)
            .fechaAsignacion(UPDATED_FECHA_ASIGNACION)
            .estado(UPDATED_ESTADO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(updatedLicenciaDigital);

        restLicenciaDigitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, licenciaDigitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(licenciaDigitalDTO))
            )
            .andExpect(status().isOk());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLicenciaDigitalToMatchAllProperties(updatedLicenciaDigital);
    }

    @Test
    @Transactional
    void putNonExistingLicenciaDigital() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        licenciaDigital.setId(longCount.incrementAndGet());

        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenciaDigitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, licenciaDigitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(licenciaDigitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLicenciaDigital() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        licenciaDigital.setId(longCount.incrementAndGet());

        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenciaDigitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(licenciaDigitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLicenciaDigital() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        licenciaDigital.setId(longCount.incrementAndGet());

        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenciaDigitalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(licenciaDigitalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLicenciaDigitalWithPatch() throws Exception {
        // Initialize the database
        insertedLicenciaDigital = licenciaDigitalRepository.saveAndFlush(licenciaDigital);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the licenciaDigital using partial update
        LicenciaDigital partialUpdatedLicenciaDigital = new LicenciaDigital();
        partialUpdatedLicenciaDigital.setId(licenciaDigital.getId());

        partialUpdatedLicenciaDigital
            .codigoLicencia(UPDATED_CODIGO_LICENCIA)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION);

        restLicenciaDigitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLicenciaDigital.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLicenciaDigital))
            )
            .andExpect(status().isOk());

        // Validate the LicenciaDigital in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLicenciaDigitalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLicenciaDigital, licenciaDigital),
            getPersistedLicenciaDigital(licenciaDigital)
        );
    }

    @Test
    @Transactional
    void fullUpdateLicenciaDigitalWithPatch() throws Exception {
        // Initialize the database
        insertedLicenciaDigital = licenciaDigitalRepository.saveAndFlush(licenciaDigital);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the licenciaDigital using partial update
        LicenciaDigital partialUpdatedLicenciaDigital = new LicenciaDigital();
        partialUpdatedLicenciaDigital.setId(licenciaDigital.getId());

        partialUpdatedLicenciaDigital
            .codigoLicencia(UPDATED_CODIGO_LICENCIA)
            .enlaceDescarga(UPDATED_ENLACE_DESCARGA)
            .fechaAsignacion(UPDATED_FECHA_ASIGNACION)
            .estado(UPDATED_ESTADO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restLicenciaDigitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLicenciaDigital.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLicenciaDigital))
            )
            .andExpect(status().isOk());

        // Validate the LicenciaDigital in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLicenciaDigitalUpdatableFieldsEquals(
            partialUpdatedLicenciaDigital,
            getPersistedLicenciaDigital(partialUpdatedLicenciaDigital)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLicenciaDigital() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        licenciaDigital.setId(longCount.incrementAndGet());

        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenciaDigitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, licenciaDigitalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(licenciaDigitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLicenciaDigital() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        licenciaDigital.setId(longCount.incrementAndGet());

        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenciaDigitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(licenciaDigitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLicenciaDigital() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        licenciaDigital.setId(longCount.incrementAndGet());

        // Create the LicenciaDigital
        LicenciaDigitalDTO licenciaDigitalDTO = licenciaDigitalMapper.toDto(licenciaDigital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenciaDigitalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(licenciaDigitalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LicenciaDigital in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLicenciaDigital() throws Exception {
        // Initialize the database
        insertedLicenciaDigital = licenciaDigitalRepository.saveAndFlush(licenciaDigital);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the licenciaDigital
        restLicenciaDigitalMockMvc
            .perform(delete(ENTITY_API_URL_ID, licenciaDigital.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return licenciaDigitalRepository.count();
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

    protected LicenciaDigital getPersistedLicenciaDigital(LicenciaDigital licenciaDigital) {
        return licenciaDigitalRepository.findById(licenciaDigital.getId()).orElseThrow();
    }

    protected void assertPersistedLicenciaDigitalToMatchAllProperties(LicenciaDigital expectedLicenciaDigital) {
        assertLicenciaDigitalAllPropertiesEquals(expectedLicenciaDigital, getPersistedLicenciaDigital(expectedLicenciaDigital));
    }

    protected void assertPersistedLicenciaDigitalToMatchUpdatableProperties(LicenciaDigital expectedLicenciaDigital) {
        assertLicenciaDigitalAllUpdatablePropertiesEquals(expectedLicenciaDigital, getPersistedLicenciaDigital(expectedLicenciaDigital));
    }
}
