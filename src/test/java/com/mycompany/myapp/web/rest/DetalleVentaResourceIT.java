package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DetalleVentaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DetalleVenta;
import com.mycompany.myapp.repository.DetalleVentaRepository;
import com.mycompany.myapp.service.dto.DetalleVentaDTO;
import com.mycompany.myapp.service.mapper.DetalleVentaMapper;
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
 * Integration tests for the {@link DetalleVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetalleVentaResourceIT {

    private static final Integer DEFAULT_CANTIDAD_VENTA = 1;
    private static final Integer UPDATED_CANTIDAD_VENTA = 2;

    private static final Float DEFAULT_PRECIO_UNITARIO = 0F;
    private static final Float UPDATED_PRECIO_UNITARIO = 1F;

    private static final Float DEFAULT_DESCUENTO = 0F;
    private static final Float UPDATED_DESCUENTO = 1F;

    private static final Float DEFAULT_SUBTOTAL = 0F;
    private static final Float UPDATED_SUBTOTAL = 1F;

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790047508639L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790047508639L);

    private static final String ENTITY_API_URL = "/api/detalle-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetalleVentaMockMvc;

    private DetalleVenta detalleVenta;

    private DetalleVenta insertedDetalleVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createEntity() {
        return new DetalleVenta()
            .cantidadVenta(DEFAULT_CANTIDAD_VENTA)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .descuento(DEFAULT_DESCUENTO)
            .subtotal(DEFAULT_SUBTOTAL)
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
    public static DetalleVenta createUpdatedEntity() {
        return new DetalleVenta()
            .cantidadVenta(UPDATED_CANTIDAD_VENTA)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .subtotal(UPDATED_SUBTOTAL)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        detalleVenta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDetalleVenta != null) {
            detalleVentaRepository.delete(insertedDetalleVenta);
            insertedDetalleVenta = null;
        }
    }

    @Test
    @Transactional
    void createDetalleVenta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);
        var returnedDetalleVentaDTO = om.readValue(
            restDetalleVentaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DetalleVentaDTO.class
        );

        // Validate the DetalleVenta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDetalleVenta = detalleVentaMapper.toEntity(returnedDetalleVentaDTO);
        assertDetalleVentaUpdatableFieldsEquals(returnedDetalleVenta, getPersistedDetalleVenta(returnedDetalleVenta));

        insertedDetalleVenta = returnedDetalleVenta;
    }

    @Test
    @Transactional
    void createDetalleVentaWithExistingId() throws Exception {
        // Create the DetalleVenta with an existing ID
        detalleVenta.setId(1L);
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadVentaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setCantidadVenta(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setPrecioUnitario(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setDescuento(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detalleVenta.setSubtotal(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetalleVentas() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidadVenta").value(hasItem(DEFAULT_CANTIDAD_VENTA)))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO.doubleValue())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getDetalleVenta() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get the detalleVenta
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, detalleVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalleVenta.getId().intValue()))
            .andExpect(jsonPath("$.cantidadVenta").value(DEFAULT_CANTIDAD_VENTA))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO.doubleValue()))
            .andExpect(jsonPath("$.descuento").value(DEFAULT_DESCUENTO.doubleValue()))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.doubleValue()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDetalleVenta() throws Exception {
        // Get the detalleVenta
        restDetalleVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDetalleVenta() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleVenta
        DetalleVenta updatedDetalleVenta = detalleVentaRepository.findById(detalleVenta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDetalleVenta are not directly saved in db
        em.detach(updatedDetalleVenta);
        updatedDetalleVenta
            .cantidadVenta(UPDATED_CANTIDAD_VENTA)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .subtotal(UPDATED_SUBTOTAL)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(updatedDetalleVenta);

        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDetalleVentaToMatchAllProperties(updatedDetalleVenta);
    }

    @Test
    @Transactional
    void putNonExistingDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta
            .cantidadVenta(UPDATED_CANTIDAD_VENTA)
            .descuento(UPDATED_DESCUENTO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION);

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetalleVentaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDetalleVenta, detalleVenta),
            getPersistedDetalleVenta(detalleVenta)
        );
    }

    @Test
    @Transactional
    void fullUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta
            .cantidadVenta(UPDATED_CANTIDAD_VENTA)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .subtotal(UPDATED_SUBTOTAL)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetalleVentaUpdatableFieldsEquals(partialUpdatedDetalleVenta, getPersistedDetalleVenta(partialUpdatedDetalleVenta));
    }

    @Test
    @Transactional
    void patchNonExistingDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalleVentaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detalleVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalleVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detalleVenta.setId(longCount.incrementAndGet());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(detalleVentaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalleVenta() throws Exception {
        // Initialize the database
        insertedDetalleVenta = detalleVentaRepository.saveAndFlush(detalleVenta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the detalleVenta
        restDetalleVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalleVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return detalleVentaRepository.count();
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

    protected DetalleVenta getPersistedDetalleVenta(DetalleVenta detalleVenta) {
        return detalleVentaRepository.findById(detalleVenta.getId()).orElseThrow();
    }

    protected void assertPersistedDetalleVentaToMatchAllProperties(DetalleVenta expectedDetalleVenta) {
        assertDetalleVentaAllPropertiesEquals(expectedDetalleVenta, getPersistedDetalleVenta(expectedDetalleVenta));
    }

    protected void assertPersistedDetalleVentaToMatchUpdatableProperties(DetalleVenta expectedDetalleVenta) {
        assertDetalleVentaAllUpdatablePropertiesEquals(expectedDetalleVenta, getPersistedDetalleVenta(expectedDetalleVenta));
    }
}
