package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LibroRepository;
import com.mycompany.myapp.service.LibroService;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Libro}.
 */
@RestController
@RequestMapping("/api/libros")
public class LibroResource {

    private static final Logger LOG = LoggerFactory.getLogger(LibroResource.class);

    private static final String ENTITY_NAME = "libro";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final LibroService libroService;

    private final LibroRepository libroRepository;

    public LibroResource(LibroService libroService, LibroRepository libroRepository) {
        this.libroService = libroService;
        this.libroRepository = libroRepository;
    }

    /**
     * {@code POST  /libros} : Create a new libro.
     *
     * @param libroDTO the libroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libroDTO, or with status {@code 400 (Bad Request)} if the libro already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LibroDTO> createLibro(@Valid @RequestBody LibroDTO libroDTO) throws URISyntaxException {
        LOG.debug("REST request to save Libro : {}", libroDTO);
        if (libroDTO.getId() != null) {
            throw new BadRequestAlertException("A new libro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        libroDTO = libroService.save(libroDTO);
        return ResponseEntity.created(new URI("/api/libros/" + libroDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, libroDTO.getId().toString()))
            .body(libroDTO);
    }

    /**
     * {@code PUT  /libros/:id} : Updates an existing libro.
     *
     * @param id the id of the libroDTO to save.
     * @param libroDTO the libroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libroDTO,
     * or with status {@code 400 (Bad Request)} if the libroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> updateLibro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LibroDTO libroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Libro : {}, {}", id, libroDTO);
        if (libroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        libroDTO = libroService.update(libroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libroDTO.getId().toString()))
            .body(libroDTO);
    }

    /**
     * {@code PATCH  /libros/:id} : Partial updates given fields of an existing libro, field will ignore if it is null
     *
     * @param id the id of the libroDTO to save.
     * @param libroDTO the libroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libroDTO,
     * or with status {@code 400 (Bad Request)} if the libroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the libroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the libroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LibroDTO> partialUpdateLibro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LibroDTO libroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Libro : {}, {}", id, libroDTO);
        if (libroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LibroDTO> result = libroService.partialUpdate(libroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /libros} : get all the Libros.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Libros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LibroDTO>> getAllLibros(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Libros");
        Page<LibroDTO> page;
        if (eagerload) {
            page = libroService.findAllWithEagerRelationships(pageable);
        } else {
            page = libroService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /libros/:id} : get the "id" libro.
     *
     * @param id the id of the libroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> getLibro(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Libro : {}", id);
        Optional<LibroDTO> libroDTO = libroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(libroDTO);
    }

    /**
     * {@code DELETE  /libros/:id} : delete the "id" libro.
     *
     * @param id the id of the libroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Libro : {}", id);
        libroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
