package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CalificacionUsuarioAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CalificacionUsuario;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.repository.CalificacionUsuarioRepository;
import com.mycompany.myapp.service.dto.CalificacionUsuarioDTO;
import com.mycompany.myapp.service.mapper.CalificacionUsuarioMapper;
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
 * Integration tests for the {@link CalificacionUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalificacionUsuarioResourceIT {

    private static final Integer DEFAULT_PUNTUACION = 1;
    private static final Integer UPDATED_PUNTUACION = 2;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CALIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CALIFICACION = Instant.ofEpochMilli(1790047508639L);

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

    private static final String ENTITY_API_URL = "/api/calificacion-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CalificacionUsuarioRepository calificacionUsuarioRepository;

    @Autowired
    private CalificacionUsuarioMapper calificacionUsuarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalificacionUsuarioMockMvc;

    private CalificacionUsuario calificacionUsuario;

    private CalificacionUsuario insertedCalificacionUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalificacionUsuario createEntity(EntityManager em) {
        CalificacionUsuario calificacionUsuario = new CalificacionUsuario()
            .puntuacion(DEFAULT_PUNTUACION)
            .comentario(DEFAULT_COMENTARIO)
            .fechaCalificacion(DEFAULT_FECHA_CALIFICACION)
            .activo(DEFAULT_ACTIVO)
            .usuarioCreacion(DEFAULT_USUARIO_CREACION)
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .usuarioModificacion(DEFAULT_USUARIO_MODIFICACION)
            .fechaModificacion(DEFAULT_FECHA_MODIFICACION);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity();
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).getFirst();
        }
        calificacionUsuario.setClienteEvaluado(usuario);
        return calificacionUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalificacionUsuario createUpdatedEntity(EntityManager em) {
        CalificacionUsuario updatedCalificacionUsuario = new CalificacionUsuario()
            .puntuacion(UPDATED_PUNTUACION)
            .comentario(UPDATED_COMENTARIO)
            .fechaCalificacion(UPDATED_FECHA_CALIFICACION)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        // Add required entity
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createUpdatedEntity();
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).getFirst();
        }
        updatedCalificacionUsuario.setClienteEvaluado(usuario);
        return updatedCalificacionUsuario;
    }

    @BeforeEach
    void initTest() {
        calificacionUsuario = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCalificacionUsuario != null) {
            calificacionUsuarioRepository.delete(insertedCalificacionUsuario);
            insertedCalificacionUsuario = null;
        }
    }

    @Test
    @Transactional
    void createCalificacionUsuario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);
        var returnedCalificacionUsuarioDTO = om.readValue(
            restCalificacionUsuarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calificacionUsuarioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CalificacionUsuarioDTO.class
        );

        // Validate the CalificacionUsuario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCalificacionUsuario = calificacionUsuarioMapper.toEntity(returnedCalificacionUsuarioDTO);
        assertCalificacionUsuarioUpdatableFieldsEquals(
            returnedCalificacionUsuario,
            getPersistedCalificacionUsuario(returnedCalificacionUsuario)
        );

        insertedCalificacionUsuario = returnedCalificacionUsuario;
    }

    @Test
    @Transactional
    void createCalificacionUsuarioWithExistingId() throws Exception {
        // Create the CalificacionUsuario with an existing ID
        calificacionUsuario.setId(1L);
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalificacionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calificacionUsuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPuntuacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        calificacionUsuario.setPuntuacion(null);

        // Create the CalificacionUsuario, which fails.
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        restCalificacionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calificacionUsuarioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaCalificacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        calificacionUsuario.setFechaCalificacion(null);

        // Create the CalificacionUsuario, which fails.
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        restCalificacionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calificacionUsuarioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        calificacionUsuario.setActivo(null);

        // Create the CalificacionUsuario, which fails.
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        restCalificacionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calificacionUsuarioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalificacionUsuarios() throws Exception {
        // Initialize the database
        insertedCalificacionUsuario = calificacionUsuarioRepository.saveAndFlush(calificacionUsuario);

        // Get all the calificacionUsuarioList
        restCalificacionUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calificacionUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].puntuacion").value(hasItem(DEFAULT_PUNTUACION)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
            .andExpect(jsonPath("$.[*].fechaCalificacion").value(hasItem(DEFAULT_FECHA_CALIFICACION.toString())))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getCalificacionUsuario() throws Exception {
        // Initialize the database
        insertedCalificacionUsuario = calificacionUsuarioRepository.saveAndFlush(calificacionUsuario);

        // Get the calificacionUsuario
        restCalificacionUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, calificacionUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calificacionUsuario.getId().intValue()))
            .andExpect(jsonPath("$.puntuacion").value(DEFAULT_PUNTUACION))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO))
            .andExpect(jsonPath("$.fechaCalificacion").value(DEFAULT_FECHA_CALIFICACION.toString()))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCalificacionUsuario() throws Exception {
        // Get the calificacionUsuario
        restCalificacionUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCalificacionUsuario() throws Exception {
        // Initialize the database
        insertedCalificacionUsuario = calificacionUsuarioRepository.saveAndFlush(calificacionUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calificacionUsuario
        CalificacionUsuario updatedCalificacionUsuario = calificacionUsuarioRepository.findById(calificacionUsuario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCalificacionUsuario are not directly saved in db
        em.detach(updatedCalificacionUsuario);
        updatedCalificacionUsuario
            .puntuacion(UPDATED_PUNTUACION)
            .comentario(UPDATED_COMENTARIO)
            .fechaCalificacion(UPDATED_FECHA_CALIFICACION)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(updatedCalificacionUsuario);

        restCalificacionUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calificacionUsuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calificacionUsuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCalificacionUsuarioToMatchAllProperties(updatedCalificacionUsuario);
    }

    @Test
    @Transactional
    void putNonExistingCalificacionUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calificacionUsuario.setId(longCount.incrementAndGet());

        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalificacionUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calificacionUsuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calificacionUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalificacionUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calificacionUsuario.setId(longCount.incrementAndGet());

        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(calificacionUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalificacionUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calificacionUsuario.setId(longCount.incrementAndGet());

        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(calificacionUsuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalificacionUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedCalificacionUsuario = calificacionUsuarioRepository.saveAndFlush(calificacionUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calificacionUsuario using partial update
        CalificacionUsuario partialUpdatedCalificacionUsuario = new CalificacionUsuario();
        partialUpdatedCalificacionUsuario.setId(calificacionUsuario.getId());

        partialUpdatedCalificacionUsuario
            .puntuacion(UPDATED_PUNTUACION)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restCalificacionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalificacionUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCalificacionUsuario))
            )
            .andExpect(status().isOk());

        // Validate the CalificacionUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCalificacionUsuarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCalificacionUsuario, calificacionUsuario),
            getPersistedCalificacionUsuario(calificacionUsuario)
        );
    }

    @Test
    @Transactional
    void fullUpdateCalificacionUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedCalificacionUsuario = calificacionUsuarioRepository.saveAndFlush(calificacionUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the calificacionUsuario using partial update
        CalificacionUsuario partialUpdatedCalificacionUsuario = new CalificacionUsuario();
        partialUpdatedCalificacionUsuario.setId(calificacionUsuario.getId());

        partialUpdatedCalificacionUsuario
            .puntuacion(UPDATED_PUNTUACION)
            .comentario(UPDATED_COMENTARIO)
            .fechaCalificacion(UPDATED_FECHA_CALIFICACION)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restCalificacionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalificacionUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCalificacionUsuario))
            )
            .andExpect(status().isOk());

        // Validate the CalificacionUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCalificacionUsuarioUpdatableFieldsEquals(
            partialUpdatedCalificacionUsuario,
            getPersistedCalificacionUsuario(partialUpdatedCalificacionUsuario)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCalificacionUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calificacionUsuario.setId(longCount.incrementAndGet());

        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalificacionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calificacionUsuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calificacionUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalificacionUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calificacionUsuario.setId(longCount.incrementAndGet());

        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(calificacionUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalificacionUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        calificacionUsuario.setId(longCount.incrementAndGet());

        // Create the CalificacionUsuario
        CalificacionUsuarioDTO calificacionUsuarioDTO = calificacionUsuarioMapper.toDto(calificacionUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(calificacionUsuarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalificacionUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalificacionUsuario() throws Exception {
        // Initialize the database
        insertedCalificacionUsuario = calificacionUsuarioRepository.saveAndFlush(calificacionUsuario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the calificacionUsuario
        restCalificacionUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, calificacionUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return calificacionUsuarioRepository.count();
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

    protected CalificacionUsuario getPersistedCalificacionUsuario(CalificacionUsuario calificacionUsuario) {
        return calificacionUsuarioRepository.findById(calificacionUsuario.getId()).orElseThrow();
    }

    protected void assertPersistedCalificacionUsuarioToMatchAllProperties(CalificacionUsuario expectedCalificacionUsuario) {
        assertCalificacionUsuarioAllPropertiesEquals(
            expectedCalificacionUsuario,
            getPersistedCalificacionUsuario(expectedCalificacionUsuario)
        );
    }

    protected void assertPersistedCalificacionUsuarioToMatchUpdatableProperties(CalificacionUsuario expectedCalificacionUsuario) {
        assertCalificacionUsuarioAllUpdatablePropertiesEquals(
            expectedCalificacionUsuario,
            getPersistedCalificacionUsuario(expectedCalificacionUsuario)
        );
    }
}
