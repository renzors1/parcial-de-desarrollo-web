package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.EditorialRepository;
import com.mycompany.myapp.service.EditorialService;
import com.mycompany.myapp.service.dto.EditorialDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Editorial}.
 */
@RestController
@RequestMapping("/api/editorials")
public class EditorialResource {

    private static final Logger LOG = LoggerFactory.getLogger(EditorialResource.class);

    private static final String ENTITY_NAME = "editorial";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final EditorialService editorialService;

    private final EditorialRepository editorialRepository;

    public EditorialResource(EditorialService editorialService, EditorialRepository editorialRepository) {
        this.editorialService = editorialService;
        this.editorialRepository = editorialRepository;
    }

    /**
     * {@code POST  /editorials} : Create a new editorial.
     *
     * @param editorialDTO the editorialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new editorialDTO, or with status {@code 400 (Bad Request)} if the editorial already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EditorialDTO> createEditorial(@Valid @RequestBody EditorialDTO editorialDTO) throws URISyntaxException {
        LOG.debug("REST request to save Editorial : {}", editorialDTO);
        if (editorialDTO.getId() != null) {
            throw new BadRequestAlertException("A new editorial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        editorialDTO = editorialService.save(editorialDTO);
        return ResponseEntity.created(new URI("/api/editorials/" + editorialDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, editorialDTO.getId().toString()))
            .body(editorialDTO);
    }

    /**
     * {@code PUT  /editorials/:id} : Updates an existing editorial.
     *
     * @param id the id of the editorialDTO to save.
     * @param editorialDTO the editorialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editorialDTO,
     * or with status {@code 400 (Bad Request)} if the editorialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the editorialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EditorialDTO> updateEditorial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EditorialDTO editorialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Editorial : {}, {}", id, editorialDTO);
        if (editorialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editorialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editorialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        editorialDTO = editorialService.update(editorialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editorialDTO.getId().toString()))
            .body(editorialDTO);
    }

    /**
     * {@code PATCH  /editorials/:id} : Partial updates given fields of an existing editorial, field will ignore if it is null
     *
     * @param id the id of the editorialDTO to save.
     * @param editorialDTO the editorialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editorialDTO,
     * or with status {@code 400 (Bad Request)} if the editorialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the editorialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the editorialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EditorialDTO> partialUpdateEditorial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EditorialDTO editorialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Editorial : {}, {}", id, editorialDTO);
        if (editorialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editorialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editorialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EditorialDTO> result = editorialService.partialUpdate(editorialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editorialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /editorials} : get all the Editorials.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Editorials in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EditorialDTO>> getAllEditorials(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Editorials");
        Page<EditorialDTO> page = editorialService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /editorials/:id} : get the "id" editorial.
     *
     * @param id the id of the editorialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the editorialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EditorialDTO> getEditorial(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Editorial : {}", id);
        Optional<EditorialDTO> editorialDTO = editorialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(editorialDTO);
    }

    /**
     * {@code DELETE  /editorials/:id} : delete the "id" editorial.
     *
     * @param id the id of the editorialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditorial(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Editorial : {}", id);
        editorialService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
