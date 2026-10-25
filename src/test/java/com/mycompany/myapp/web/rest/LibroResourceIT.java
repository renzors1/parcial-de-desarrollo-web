package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LibroAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.repository.LibroRepository;
import com.mycompany.myapp.service.LibroService;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.service.mapper.LibroMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link LibroResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LibroResourceIT {

    private static final String DEFAULT_ISBN = "AAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_SINOPSIS = "AAAAAAAAAA";
    private static final String UPDATED_SINOPSIS = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PORTADA = "AAAAAAAAAA";
    private static final String UPDATED_URL_PORTADA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_PUBLICACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PUBLICACION = LocalDate.parse("2026-09-22");

    private static final Float DEFAULT_PRECIO = 0F;
    private static final Float UPDATED_PRECIO = 1F;

    private static final String DEFAULT_IDIOMA = "AAAAAAAAAA";
    private static final String UPDATED_IDIOMA = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/libros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LibroRepository libroRepository;

    @Mock
    private LibroRepository libroRepositoryMock;

    @Autowired
    private LibroMapper libroMapper;

    @Mock
    private LibroService libroServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibroMockMvc;

    private Libro libro;

    private Libro insertedLibro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createEntity() {
        return new Libro()
            .isbn(DEFAULT_ISBN)
            .titulo(DEFAULT_TITULO)
            .sinopsis(DEFAULT_SINOPSIS)
            .urlPortada(DEFAULT_URL_PORTADA)
            .fechaPublicacion(DEFAULT_FECHA_PUBLICACION)
            .precio(DEFAULT_PRECIO)
            .idioma(DEFAULT_IDIOMA)
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
    public static Libro createUpdatedEntity() {
        return new Libro()
            .isbn(UPDATED_ISBN)
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .urlPortada(UPDATED_URL_PORTADA)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .precio(UPDATED_PRECIO)
            .idioma(UPDATED_IDIOMA)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
    }

    @BeforeEach
    void initTest() {
        libro = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLibro != null) {
            libroRepository.delete(insertedLibro);
            insertedLibro = null;
        }
    }

    @Test
    @Transactional
    void createLibro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);
        var returnedLibroDTO = om.readValue(
            restLibroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LibroDTO.class
        );

        // Validate the Libro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLibro = libroMapper.toEntity(returnedLibroDTO);
        assertLibroUpdatableFieldsEquals(returnedLibro, getPersistedLibro(returnedLibro));

        insertedLibro = returnedLibro;
    }

    @Test
    @Transactional
    void createLibroWithExistingId() throws Exception {
        // Create the Libro with an existing ID
        libro.setId(1L);
        LibroDTO libroDTO = libroMapper.toDto(libro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsbnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setIsbn(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setTitulo(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setPrecio(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdiomaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setIdioma(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        libro.setActivo(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLibros() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get all the libroList
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libro.getId().intValue())))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].sinopsis").value(hasItem(DEFAULT_SINOPSIS)))
            .andExpect(jsonPath("$.[*].urlPortada").value(hasItem(DEFAULT_URL_PORTADA)))
            .andExpect(jsonPath("$.[*].fechaPublicacion").value(hasItem(DEFAULT_FECHA_PUBLICACION.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].idioma").value(hasItem(DEFAULT_IDIOMA)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].usuarioCreacion").value(hasItem(DEFAULT_USUARIO_CREACION)))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].usuarioModificacion").value(hasItem(DEFAULT_USUARIO_MODIFICACION)))
            .andExpect(jsonPath("$.[*].fechaModificacion").value(hasItem(DEFAULT_FECHA_MODIFICACION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLibrosWithEagerRelationshipsIsEnabled() throws Exception {
        when(libroServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLibroMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(libroServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLibrosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(libroServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLibroMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(libroRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLibro() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        // Get the libro
        restLibroMockMvc
            .perform(get(ENTITY_API_URL_ID, libro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libro.getId().intValue()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.sinopsis").value(DEFAULT_SINOPSIS))
            .andExpect(jsonPath("$.urlPortada").value(DEFAULT_URL_PORTADA))
            .andExpect(jsonPath("$.fechaPublicacion").value(DEFAULT_FECHA_PUBLICACION.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.idioma").value(DEFAULT_IDIOMA))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.usuarioCreacion").value(DEFAULT_USUARIO_CREACION))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.usuarioModificacion").value(DEFAULT_USUARIO_MODIFICACION))
            .andExpect(jsonPath("$.fechaModificacion").value(DEFAULT_FECHA_MODIFICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLibro() throws Exception {
        // Get the libro
        restLibroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLibro() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libro
        Libro updatedLibro = libroRepository.findById(libro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLibro are not directly saved in db
        em.detach(updatedLibro);
        updatedLibro
            .isbn(UPDATED_ISBN)
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .urlPortada(UPDATED_URL_PORTADA)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .precio(UPDATED_PRECIO)
            .idioma(UPDATED_IDIOMA)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);
        LibroDTO libroDTO = libroMapper.toDto(updatedLibro);

        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libroDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLibroToMatchAllProperties(updatedLibro);
    }

    @Test
    @Transactional
    void putNonExistingLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libroDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro
            .isbn(UPDATED_ISBN)
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibroUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLibro, libro), getPersistedLibro(libro));
    }

    @Test
    @Transactional
    void fullUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro
            .isbn(UPDATED_ISBN)
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .urlPortada(UPDATED_URL_PORTADA)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .precio(UPDATED_PRECIO)
            .idioma(UPDATED_IDIOMA)
            .activo(UPDATED_ACTIVO)
            .usuarioCreacion(UPDATED_USUARIO_CREACION)
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .usuarioModificacion(UPDATED_USUARIO_MODIFICACION)
            .fechaModificacion(UPDATED_FECHA_MODIFICACION);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibroUpdatableFieldsEquals(partialUpdatedLibro, getPersistedLibro(partialUpdatedLibro));
    }

    @Test
    @Transactional
    void patchNonExistingLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        libro.setId(longCount.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(libroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibro() throws Exception {
        // Initialize the database
        insertedLibro = libroRepository.saveAndFlush(libro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the libro
        restLibroMockMvc
            .perform(delete(ENTITY_API_URL_ID, libro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return libroRepository.count();
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

    protected Libro getPersistedLibro(Libro libro) {
        return libroRepository.findById(libro.getId()).orElseThrow();
    }

    protected void assertPersistedLibroToMatchAllProperties(Libro expectedLibro) {
        assertLibroAllPropertiesEquals(expectedLibro, getPersistedLibro(expectedLibro));
    }

    protected void assertPersistedLibroToMatchUpdatableProperties(Libro expectedLibro) {
        assertLibroAllUpdatablePropertiesEquals(expectedLibro, getPersistedLibro(expectedLibro));
    }
}
