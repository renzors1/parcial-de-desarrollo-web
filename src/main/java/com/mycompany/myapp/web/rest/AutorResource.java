package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AutorRepository;
import com.mycompany.myapp.service.AutorService;
import com.mycompany.myapp.service.dto.AutorDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Autor}.
 */
@RestController
@RequestMapping("/api/autors")
public class AutorResource {

    private static final Logger LOG = LoggerFactory.getLogger(AutorResource.class);

    private static final String ENTITY_NAME = "autor";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final AutorService autorService;

    private final AutorRepository autorRepository;

    public AutorResource(AutorService autorService, AutorRepository autorRepository) {
        this.autorService = autorService;
        this.autorRepository = autorRepository;
    }

    /**
     * {@code POST  /autors} : Create a new autor.
     *
     * @param autorDTO the autorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autorDTO, or with status {@code 400 (Bad Request)} if the autor already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AutorDTO> createAutor(@Valid @RequestBody AutorDTO autorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Autor : {}", autorDTO);
        if (autorDTO.getId() != null) {
            throw new BadRequestAlertException("A new autor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        autorDTO = autorService.save(autorDTO);
        return ResponseEntity.created(new URI("/api/autors/" + autorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, autorDTO.getId().toString()))
            .body(autorDTO);
    }

    /**
     * {@code PUT  /autors/:id} : Updates an existing autor.
     *
     * @param id the id of the autorDTO to save.
     * @param autorDTO the autorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autorDTO,
     * or with status {@code 400 (Bad Request)} if the autorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutorDTO> updateAutor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AutorDTO autorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Autor : {}, {}", id, autorDTO);
        if (autorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        autorDTO = autorService.update(autorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autorDTO.getId().toString()))
            .body(autorDTO);
    }

    /**
     * {@code PATCH  /autors/:id} : Partial updates given fields of an existing autor, field will ignore if it is null
     *
     * @param id the id of the autorDTO to save.
     * @param autorDTO the autorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autorDTO,
     * or with status {@code 400 (Bad Request)} if the autorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the autorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the autorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AutorDTO> partialUpdateAutor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AutorDTO autorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Autor : {}, {}", id, autorDTO);
        if (autorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AutorDTO> result = autorService.partialUpdate(autorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /autors} : get all the Autors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Autors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AutorDTO>> getAllAutors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Autors");
        Page<AutorDTO> page = autorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autors/:id} : get the "id" autor.
     *
     * @param id the id of the autorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> getAutor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Autor : {}", id);
        Optional<AutorDTO> autorDTO = autorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autorDTO);
    }

    /**
     * {@code DELETE  /autors/:id} : delete the "id" autor.
     *
     * @param id the id of the autorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Autor : {}", id);
        autorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
