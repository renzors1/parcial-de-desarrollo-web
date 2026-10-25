package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PromocionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Promocion}.
 */
public interface PromocionService {
    /**
     * Save a promocion.
     *
     * @param promocionDTO the entity to save.
     * @return the persisted entity.
     */
    PromocionDTO save(PromocionDTO promocionDTO);

    /**
     * Updates a promocion.
     *
     * @param promocionDTO the entity to update.
     * @return the persisted entity.
     */
    PromocionDTO update(PromocionDTO promocionDTO);

    /**
     * Partially updates a promocion.
     *
     * @param promocionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PromocionDTO> partialUpdate(PromocionDTO promocionDTO);

    /**
     * Get all the promocions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PromocionDTO> findAll(Pageable pageable);

    /**
     * Get all the promocions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PromocionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" promocion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PromocionDTO> findOne(Long id);

    /**
     * Delete the "id" promocion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
