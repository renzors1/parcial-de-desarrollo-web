package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PromocionRepository;
import com.mycompany.myapp.service.PromocionService;
import com.mycompany.myapp.service.dto.PromocionDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Promocion}.
 */
@RestController
@RequestMapping("/api/promocions")
public class PromocionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PromocionResource.class);

    private static final String ENTITY_NAME = "promocion";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final PromocionService promocionService;

    private final PromocionRepository promocionRepository;

    public PromocionResource(PromocionService promocionService, PromocionRepository promocionRepository) {
        this.promocionService = promocionService;
        this.promocionRepository = promocionRepository;
    }

    /**
     * {@code POST  /promocions} : Create a new promocion.
     *
     * @param promocionDTO the promocionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promocionDTO, or with status {@code 400 (Bad Request)} if the promocion already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PromocionDTO> createPromocion(@Valid @RequestBody PromocionDTO promocionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Promocion : {}", promocionDTO);
        if (promocionDTO.getId() != null) {
            throw new BadRequestAlertException("A new promocion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        promocionDTO = promocionService.save(promocionDTO);
        return ResponseEntity.created(new URI("/api/promocions/" + promocionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, promocionDTO.getId().toString()))
            .body(promocionDTO);
    }

    /**
     * {@code PUT  /promocions/:id} : Updates an existing promocion.
     *
     * @param id the id of the promocionDTO to save.
     * @param promocionDTO the promocionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocionDTO,
     * or with status {@code 400 (Bad Request)} if the promocionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promocionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> updatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PromocionDTO promocionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Promocion : {}, {}", id, promocionDTO);
        if (promocionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promocionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        promocionDTO = promocionService.update(promocionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promocionDTO.getId().toString()))
            .body(promocionDTO);
    }

    /**
     * {@code PATCH  /promocions/:id} : Partial updates given fields of an existing promocion, field will ignore if it is null
     *
     * @param id the id of the promocionDTO to save.
     * @param promocionDTO the promocionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocionDTO,
     * or with status {@code 400 (Bad Request)} if the promocionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promocionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promocionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromocionDTO> partialUpdatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PromocionDTO promocionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Promocion : {}, {}", id, promocionDTO);
        if (promocionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promocionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromocionDTO> result = promocionService.partialUpdate(promocionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promocionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promocions} : get all the Promocions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Promocions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PromocionDTO>> getAllPromocions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Promocions");
        Page<PromocionDTO> page;
        if (eagerload) {
            page = promocionService.findAllWithEagerRelationships(pageable);
        } else {
            page = promocionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promocions/:id} : get the "id" promocion.
     *
     * @param id the id of the promocionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promocionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> getPromocion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Promocion : {}", id);
        Optional<PromocionDTO> promocionDTO = promocionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promocionDTO);
    }

    /**
     * {@code DELETE  /promocions/:id} : delete the "id" promocion.
     *
     * @param id the id of the promocionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromocion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Promocion : {}", id);
        promocionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
