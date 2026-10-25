package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LicenciaDigitalRepository;
import com.mycompany.myapp.service.LicenciaDigitalService;
import com.mycompany.myapp.service.dto.LicenciaDigitalDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LicenciaDigital}.
 */
@RestController
@RequestMapping("/api/licencia-digitals")
public class LicenciaDigitalResource {

    private static final Logger LOG = LoggerFactory.getLogger(LicenciaDigitalResource.class);

    private static final String ENTITY_NAME = "licenciaDigital";

    @Value("${jhipster.clientApp.name:ejemplo01}")
    private String applicationName;

    private final LicenciaDigitalService licenciaDigitalService;

    private final LicenciaDigitalRepository licenciaDigitalRepository;

    public LicenciaDigitalResource(LicenciaDigitalService licenciaDigitalService, LicenciaDigitalRepository licenciaDigitalRepository) {
        this.licenciaDigitalService = licenciaDigitalService;
        this.licenciaDigitalRepository = licenciaDigitalRepository;
    }

    /**
     * {@code POST  /licencia-digitals} : Create a new licenciaDigital.
     *
     * @param licenciaDigitalDTO the licenciaDigitalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new licenciaDigitalDTO, or with status {@code 400 (Bad Request)} if the licenciaDigital already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LicenciaDigitalDTO> createLicenciaDigital(@Valid @RequestBody LicenciaDigitalDTO licenciaDigitalDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LicenciaDigital : {}", licenciaDigitalDTO);
        if (licenciaDigitalDTO.getId() != null) {
            throw new BadRequestAlertException("A new licenciaDigital cannot already have an ID", ENTITY_NAME, "idexists");
        }
        licenciaDigitalDTO = licenciaDigitalService.save(licenciaDigitalDTO);
        return ResponseEntity.created(new URI("/api/licencia-digitals/" + licenciaDigitalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, licenciaDigitalDTO.getId().toString()))
            .body(licenciaDigitalDTO);
    }

    /**
     * {@code PUT  /licencia-digitals/:id} : Updates an existing licenciaDigital.
     *
     * @param id the id of the licenciaDigitalDTO to save.
     * @param licenciaDigitalDTO the licenciaDigitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated licenciaDigitalDTO,
     * or with status {@code 400 (Bad Request)} if the licenciaDigitalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the licenciaDigitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LicenciaDigitalDTO> updateLicenciaDigital(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LicenciaDigitalDTO licenciaDigitalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LicenciaDigital : {}, {}", id, licenciaDigitalDTO);
        if (licenciaDigitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, licenciaDigitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!licenciaDigitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        licenciaDigitalDTO = licenciaDigitalService.update(licenciaDigitalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, licenciaDigitalDTO.getId().toString()))
            .body(licenciaDigitalDTO);
    }

    /**
     * {@code PATCH  /licencia-digitals/:id} : Partial updates given fields of an existing licenciaDigital, field will ignore if it is null
     *
     * @param id the id of the licenciaDigitalDTO to save.
     * @param licenciaDigitalDTO the licenciaDigitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated licenciaDigitalDTO,
     * or with status {@code 400 (Bad Request)} if the licenciaDigitalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the licenciaDigitalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the licenciaDigitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LicenciaDigitalDTO> partialUpdateLicenciaDigital(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LicenciaDigitalDTO licenciaDigitalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update LicenciaDigital : {}, {}", id, licenciaDigitalDTO);
        if (licenciaDigitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, licenciaDigitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!licenciaDigitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LicenciaDigitalDTO> result = licenciaDigitalService.partialUpdate(licenciaDigitalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, licenciaDigitalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /licencia-digitals} : get all the Licencia Digitals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Licencia Digitals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LicenciaDigitalDTO>> getAllLicenciaDigitals(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of LicenciaDigitals");
        Page<LicenciaDigitalDTO> page = licenciaDigitalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /licencia-digitals/:id} : get the "id" licenciaDigital.
     *
     * @param id the id of the licenciaDigitalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the licenciaDigitalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LicenciaDigitalDTO> getLicenciaDigital(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LicenciaDigital : {}", id);
        Optional<LicenciaDigitalDTO> licenciaDigitalDTO = licenciaDigitalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(licenciaDigitalDTO);
    }

    /**
     * {@code DELETE  /licencia-digitals/:id} : delete the "id" licenciaDigital.
     *
     * @param id the id of the licenciaDigitalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLicenciaDigital(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LicenciaDigital : {}", id);
        licenciaDigitalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
