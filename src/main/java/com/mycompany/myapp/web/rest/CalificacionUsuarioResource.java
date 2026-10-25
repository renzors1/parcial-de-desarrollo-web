package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CalificacionUsuarioRepository;
import com.mycompany.myapp.service.CalificacionUsuarioService;
import com.mycompany.myapp.service.dto.CalificacionUsuarioDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CalificacionUsuario}.
 */
@RestController
@RequestMapping("/api/calificacion-usuarios")
public class CalificacionUsuarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(CalificacionUsuarioResource.class);

    private static final String ENTITY_NAME = "calificacionUsuario";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final CalificacionUsuarioService calificacionUsuarioService;

    private final CalificacionUsuarioRepository calificacionUsuarioRepository;

    public CalificacionUsuarioResource(
        CalificacionUsuarioService calificacionUsuarioService,
        CalificacionUsuarioRepository calificacionUsuarioRepository
    ) {
        this.calificacionUsuarioService = calificacionUsuarioService;
        this.calificacionUsuarioRepository = calificacionUsuarioRepository;
    }

    /**
     * {@code POST  /calificacion-usuarios} : Create a new calificacionUsuario.
     *
     * @param calificacionUsuarioDTO the calificacionUsuarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calificacionUsuarioDTO, or with status {@code 400 (Bad Request)} if the calificacionUsuario already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CalificacionUsuarioDTO> createCalificacionUsuario(
        @Valid @RequestBody CalificacionUsuarioDTO calificacionUsuarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CalificacionUsuario : {}", calificacionUsuarioDTO);
        if (calificacionUsuarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new calificacionUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        calificacionUsuarioDTO = calificacionUsuarioService.save(calificacionUsuarioDTO);
        return ResponseEntity.created(new URI("/api/calificacion-usuarios/" + calificacionUsuarioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, calificacionUsuarioDTO.getId().toString()))
            .body(calificacionUsuarioDTO);
    }

    /**
     * {@code PUT  /calificacion-usuarios/:id} : Updates an existing calificacionUsuario.
     *
     * @param id the id of the calificacionUsuarioDTO to save.
     * @param calificacionUsuarioDTO the calificacionUsuarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calificacionUsuarioDTO,
     * or with status {@code 400 (Bad Request)} if the calificacionUsuarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calificacionUsuarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalificacionUsuarioDTO> updateCalificacionUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CalificacionUsuarioDTO calificacionUsuarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CalificacionUsuario : {}, {}", id, calificacionUsuarioDTO);
        if (calificacionUsuarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calificacionUsuarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calificacionUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        calificacionUsuarioDTO = calificacionUsuarioService.update(calificacionUsuarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calificacionUsuarioDTO.getId().toString()))
            .body(calificacionUsuarioDTO);
    }

    /**
     * {@code PATCH  /calificacion-usuarios/:id} : Partial updates given fields of an existing calificacionUsuario, field will ignore if it is null
     *
     * @param id the id of the calificacionUsuarioDTO to save.
     * @param calificacionUsuarioDTO the calificacionUsuarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calificacionUsuarioDTO,
     * or with status {@code 400 (Bad Request)} if the calificacionUsuarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the calificacionUsuarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the calificacionUsuarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CalificacionUsuarioDTO> partialUpdateCalificacionUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CalificacionUsuarioDTO calificacionUsuarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CalificacionUsuario : {}, {}", id, calificacionUsuarioDTO);
        if (calificacionUsuarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calificacionUsuarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calificacionUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CalificacionUsuarioDTO> result = calificacionUsuarioService.partialUpdate(calificacionUsuarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calificacionUsuarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /calificacion-usuarios} : get all the Calificacion Usuarios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Calificacion Usuarios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CalificacionUsuarioDTO>> getAllCalificacionUsuarios(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CalificacionUsuarios");
        Page<CalificacionUsuarioDTO> page = calificacionUsuarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calificacion-usuarios/:id} : get the "id" calificacionUsuario.
     *
     * @param id the id of the calificacionUsuarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calificacionUsuarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionUsuarioDTO> getCalificacionUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CalificacionUsuario : {}", id);
        Optional<CalificacionUsuarioDTO> calificacionUsuarioDTO = calificacionUsuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calificacionUsuarioDTO);
    }

    /**
     * {@code DELETE  /calificacion-usuarios/:id} : delete the "id" calificacionUsuario.
     *
     * @param id the id of the calificacionUsuarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacionUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CalificacionUsuario : {}", id);
        calificacionUsuarioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
