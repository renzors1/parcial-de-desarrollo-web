package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RolAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.repository.RolRepository;
import com.mycompany.myapp.service.dto.RolDTO;
import com.mycompany.myapp.service.mapper.RolMapper;
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
 * Integration tests for the {@link RolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RolResourceIT {

    private static final Integer DEFAULT_CODIGO_IDENTIFICADOR = 1;
    private static final Integer UPDATED_CODIGO_IDENTIFICADOR = 2;

    private static final String DEFAULT_NOMBRE_ROL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ROL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/rols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RolMapper rolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRolMockMvc;

    private Rol rol;

    private Rol insertedRol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rol createEntity() {
        return new Rol()
            .codigoIdentificador(DEFAULT_CODIGO_IDENTIFICADOR)
            .nombreRol(DEFAULT_NOMBRE_ROL)
            .descripcion(DEFAULT_DESCRIPCION)
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
    public static Rol createUpdatedEntity() {
        return new Rol()
            .codigoIdentificador(UPDATED_CODIGO_IDENTIFICADOR)
            .nombreRol(UPDATED_NOMBRE_ROL)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        rol = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRol != null) {
            rolRepository.delete(insertedRol);
            insertedRol = null;
        }
    }

    @Test
    @Transactional
    void createRol() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);
        var returnedRolDTO = om.readValue(
            restRolMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RolDTO.class
        );

        // Validate the Rol in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRol = rolMapper.toEntity(returnedRolDTO);
        assertRolUpdatableFieldsEquals(returnedRol, getPersistedRol(returnedRol));

        insertedRol = returnedRol;
    }

    @Test
    @Transactional
    void createRolWithExistingId() throws Exception {
        // Create the Rol with an existing ID
        rol.setId(1L);
        RolDTO rolDTO = rolMapper.toDto(rol);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rol.setCodigoIdentificador(null);

        // Create the Rol, which fails.
        RolDTO rolDTO = rolMapper.toDto(rol);

        restRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreRolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rol.setNombreRol(null);

        // Create the Rol, which fails.
        RolDTO rolDTO = rolMapper.toDto(rol);

        restRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rol.setActivo(null);

        // Create the Rol, which fails.
        RolDTO rolDTO = rolMapper.toDto(rol);

        restRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRols() throws Exception {
        // Initialize the database
        insertedRol = rolRepository.saveAndFlush(rol);

        // Get all the rolList
        restRolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rol.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoIdentificador").value(hasItem(DEFAULT_CODIGO_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].nombreRol").value(hasItem(DEFAULT_NOMBRE_ROL)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getRol() throws Exception {
        // Initialize the database
        insertedRol = rolRepository.saveAndFlush(rol);

        // Get the rol
        restRolMockMvc
            .perform(get(ENTITY_API_URL_ID, rol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rol.getId().intValue()))
            .andExpect(jsonPath("$.codigoIdentificador").value(DEFAULT_CODIGO_IDENTIFICADOR))
            .andExpect(jsonPath("$.nombreRol").value(DEFAULT_NOMBRE_ROL))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRol() throws Exception {
        // Get the rol
        restRolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRol() throws Exception {
        // Initialize the database
        insertedRol = rolRepository.saveAndFlush(rol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rol
        Rol updatedRol = rolRepository.findById(rol.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRol are not directly saved in db
        em.detach(updatedRol);
        updatedRol
            .codigoIdentificador(UPDATED_CODIGO_IDENTIFICADOR)
            .nombreRol(UPDATED_NOMBRE_ROL)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        RolDTO rolDTO = rolMapper.toDto(updatedRol);

        restRolMockMvc
            .perform(put(ENTITY_API_URL_ID, rolDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isOk());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRolToMatchAllProperties(updatedRol);
    }

    @Test
    @Transactional
    void putNonExistingRol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rol.setId(longCount.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(put(ENTITY_API_URL_ID, rolDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rol.setId(longCount.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rol.setId(longCount.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRolWithPatch() throws Exception {
        // Initialize the database
        insertedRol = rolRepository.saveAndFlush(rol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rol using partial update
        Rol partialUpdatedRol = new Rol();
        partialUpdatedRol.setId(rol.getId());

        partialUpdatedRol
            .codigoIdentificador(UPDATED_CODIGO_IDENTIFICADOR)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRol))
            )
            .andExpect(status().isOk());

        // Validate the Rol in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRolUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRol, rol), getPersistedRol(rol));
    }

    @Test
    @Transactional
    void fullUpdateRolWithPatch() throws Exception {
        // Initialize the database
        insertedRol = rolRepository.saveAndFlush(rol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rol using partial update
        Rol partialUpdatedRol = new Rol();
        partialUpdatedRol.setId(rol.getId());

        partialUpdatedRol
            .codigoIdentificador(UPDATED_CODIGO_IDENTIFICADOR)
            .nombreRol(UPDATED_NOMBRE_ROL)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRol))
            )
            .andExpect(status().isOk());

        // Validate the Rol in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRolUpdatableFieldsEquals(partialUpdatedRol, getPersistedRol(partialUpdatedRol));
    }

    @Test
    @Transactional
    void patchNonExistingRol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rol.setId(longCount.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rolDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rol.setId(longCount.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rol.setId(longCount.incrementAndGet());

        // Create the Rol
        RolDTO rolDTO = rolMapper.toDto(rol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rolDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRol() throws Exception {
        // Initialize the database
        insertedRol = rolRepository.saveAndFlush(rol);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rol
        restRolMockMvc.perform(delete(ENTITY_API_URL_ID, rol.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rolRepository.count();
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

    protected Rol getPersistedRol(Rol rol) {
        return rolRepository.findById(rol.getId()).orElseThrow();
    }

    protected void assertPersistedRolToMatchAllProperties(Rol expectedRol) {
        assertRolAllPropertiesEquals(expectedRol, getPersistedRol(expectedRol));
    }

    protected void assertPersistedRolToMatchUpdatableProperties(Rol expectedRol) {
        assertRolAllUpdatablePropertiesEquals(expectedRol, getPersistedRol(expectedRol));
    }
}
