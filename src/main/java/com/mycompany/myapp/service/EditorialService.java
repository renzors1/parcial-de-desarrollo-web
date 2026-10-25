package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EditorialDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Editorial}.
 */
public interface EditorialService {
    /**
     * Save a editorial.
     *
     * @param editorialDTO the entity to save.
     * @return the persisted entity.
     */
    EditorialDTO save(EditorialDTO editorialDTO);

    /**
     * Updates a editorial.
     *
     * @param editorialDTO the entity to update.
     * @return the persisted entity.
     */
    EditorialDTO update(EditorialDTO editorialDTO);

    /**
     * Partially updates a editorial.
     *
     * @param editorialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EditorialDTO> partialUpdate(EditorialDTO editorialDTO);

    /**
     * Get all the editorials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EditorialDTO> findAll(Pageable pageable);

    /**
     * Get the "id" editorial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EditorialDTO> findOne(Long id);

    /**
     * Delete the "id" editorial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
