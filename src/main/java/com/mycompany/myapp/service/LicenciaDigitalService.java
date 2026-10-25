package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.LicenciaDigitalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.LicenciaDigital}.
 */
public interface LicenciaDigitalService {
    /**
     * Save a licenciaDigital.
     *
     * @param licenciaDigitalDTO the entity to save.
     * @return the persisted entity.
     */
    LicenciaDigitalDTO save(LicenciaDigitalDTO licenciaDigitalDTO);

    /**
     * Updates a licenciaDigital.
     *
     * @param licenciaDigitalDTO the entity to update.
     * @return the persisted entity.
     */
    LicenciaDigitalDTO update(LicenciaDigitalDTO licenciaDigitalDTO);

    /**
     * Partially updates a licenciaDigital.
     *
     * @param licenciaDigitalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LicenciaDigitalDTO> partialUpdate(LicenciaDigitalDTO licenciaDigitalDTO);

    /**
     * Get all the licenciaDigitals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LicenciaDigitalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" licenciaDigital.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LicenciaDigitalDTO> findOne(Long id);

    /**
     * Delete the "id" licenciaDigital.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
