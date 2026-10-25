package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.VentaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.repository.VentaRepository;
import com.mycompany.myapp.service.dto.VentaDTO;
import com.mycompany.myapp.service.mapper.VentaMapper;
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
 * Integration tests for the {@link VentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VentaResourceIT {

    private static final String DEFAULT_NUMERO_COMPROBANTE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_COMPROBANTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_VENTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_VENTA = Instant.ofEpochMilli(1790047508639L);

    private static final Float DEFAULT_SUBTOTAL_VENTA = 0F;
    private static final Float UPDATED_SUBTOTAL_VENTA = 1F;

    private static final Float DEFAULT_TOTAL_DESCUENTO = 0F;
    private static final Float UPDATED_TOTAL_DESCUENTO = 1F;

    private static final Float DEFAULT_IGV = 0F;
    private static final Float UPDATED_IGV = 1F;

    private static final Float DEFAULT_TOTAL_VENTA = 0F;
    private static final Float UPDATED_TOTAL_VENTA = 1F;

    private static final String DEFAULT_USUARIO_CREACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_CREACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1790047508639L);

    private static final String DEFAULT_USUARIO_MODIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_MODIFICACION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_MODIFICACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_MODIFICACION = Instant.ofEpochMilli(1790047508639L);

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentaMockMvc;

    private Venta venta;

    private Venta insertedVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createEntity() {
        return new Venta()
            .numeroComprobante(DEFAULT_NUMERO_COMPROBANTE)
            .fechaVenta(DEFAULT_FECHA_VENTA)
            .subtotalVenta(DEFAULT_SUBTOTAL_VENTA)
            .totalDescuento(DEFAULT_TOTAL_DESCUENTO)
            .igv(DEFAULT_IGV)
            .totalVenta(DEFAULT_TOTAL_VENTA)
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
    public static Venta createUpdatedEntity() {
        return new Venta()
            .numeroComprobante(UPDATED_NUMERO_COMPROBANTE)
            .fechaVenta(UPDATED_FECHA_VENTA)
            .subtotalVenta(UPDATED_SUBTOTAL_VENTA)
            .totalDescuento(UPDATED_TOTAL_DESCUENTO)
            .igv(UPDATED_IGV)
            .totalVenta(UPDATED_TOTAL_VENTA)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        venta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVenta != null) {
            ventaRepository.delete(insertedVenta);
            insertedVenta = null;
        }
    }

    @Test
    @Transactional
    void createVenta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);
        var returnedVentaDTO = om.readValue(
            restVentaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VentaDTO.class
        );

        // Validate the Venta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVenta = ventaMapper.toEntity(returnedVentaDTO);
        assertVentaUpdatableFieldsEquals(returnedVenta, getPersistedVenta(returnedVenta));

        insertedVenta = returnedVenta;
    }

    @Test
    @Transactional
    void createVentaWithExistingId() throws Exception {
        // Create the Venta with an existing ID
        venta.setId(1L);
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroComprobanteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setNumeroComprobante(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaVentaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setFechaVenta(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalVentaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setSubtotalVenta(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setTotalDescuento(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIgvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setIgv(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalVentaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        venta.setTotalVenta(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVentas() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get all the ventaList
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroComprobante").value(hasItem(DEFAULT_NUMERO_COMPROBANTE)))
            .andExpect(jsonPath("$.[*].fechaVenta").value(hasItem(DEFAULT_FECHA_VENTA.toString())))
            .andExpect(jsonPath("$.[*].subtotalVenta").value(hasItem(DEFAULT_SUBTOTAL_VENTA.doubleValue())))
            .andExpect(jsonPath("$.[*].totalDescuento").value(hasItem(DEFAULT_TOTAL_DESCUENTO.doubleValue())))
            .andExpect(jsonPath("$.[*].igv").value(hasItem(DEFAULT_IGV.doubleValue())))
            .andExpect(jsonPath("$.[*].totalVenta").value(hasItem(DEFAULT_TOTAL_VENTA.doubleValue())))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @Test
    @Transactional
    void getVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        // Get the venta
        restVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, venta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venta.getId().intValue()))
            .andExpect(jsonPath("$.numeroComprobante").value(DEFAULT_NUMERO_COMPROBANTE))
            .andExpect(jsonPath("$.fechaVenta").value(DEFAULT_FECHA_VENTA.toString()))
            .andExpect(jsonPath("$.subtotalVenta").value(DEFAULT_SUBTOTAL_VENTA.doubleValue()))
            .andExpect(jsonPath("$.totalDescuento").value(DEFAULT_TOTAL_DESCUENTO.doubleValue()))
            .andExpect(jsonPath("$.igv").value(DEFAULT_IGV.doubleValue()))
            .andExpect(jsonPath("$.totalVenta").value(DEFAULT_TOTAL_VENTA.doubleValue()))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVenta() throws Exception {
        // Get the venta
        restVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta
        Venta updatedVenta = ventaRepository.findById(venta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVenta are not directly saved in db
        em.detach(updatedVenta);
        updatedVenta
            .numeroComprobante(UPDATED_NUMERO_COMPROBANTE)
            .fechaVenta(UPDATED_FECHA_VENTA)
            .subtotalVenta(UPDATED_SUBTOTAL_VENTA)
            .totalDescuento(UPDATED_TOTAL_DESCUENTO)
            .igv(UPDATED_IGV)
            .totalVenta(UPDATED_TOTAL_VENTA)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        VentaDTO ventaDTO = ventaMapper.toDto(updatedVenta);

        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVentaToMatchAllProperties(updatedVenta);
    }

    @Test
    @Transactional
    void putNonExistingVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta
            .numeroComprobante(UPDATED_NUMERO_COMPROBANTE)
            .fechaVenta(UPDATED_FECHA_VENTA)
            .igv(UPDATED_IGV)
            .totalVenta(UPDATED_TOTAL_VENTA);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVenta, venta), getPersistedVenta(venta));
    }

    @Test
    @Transactional
    void fullUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta
            .numeroComprobante(UPDATED_NUMERO_COMPROBANTE)
            .fechaVenta(UPDATED_FECHA_VENTA)
            .subtotalVenta(UPDATED_SUBTOTAL_VENTA)
            .totalDescuento(UPDATED_TOTAL_DESCUENTO)
            .igv(UPDATED_IGV)
            .totalVenta(UPDATED_TOTAL_VENTA)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVentaUpdatableFieldsEquals(partialUpdatedVenta, getPersistedVenta(partialUpdatedVenta));
    }

    @Test
    @Transactional
    void patchNonExistingVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ventaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ventaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        venta.setId(longCount.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ventaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenta() throws Exception {
        // Initialize the database
        insertedVenta = ventaRepository.saveAndFlush(venta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the venta
        restVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, venta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ventaRepository.count();
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

    protected Venta getPersistedVenta(Venta venta) {
        return ventaRepository.findById(venta.getId()).orElseThrow();
    }

    protected void assertPersistedVentaToMatchAllProperties(Venta expectedVenta) {
        assertVentaAllPropertiesEquals(expectedVenta, getPersistedVenta(expectedVenta));
    }

    protected void assertPersistedVentaToMatchUpdatableProperties(Venta expectedVenta) {
        assertVentaAllUpdatablePropertiesEquals(expectedVenta, getPersistedVenta(expectedVenta));
    }
}
