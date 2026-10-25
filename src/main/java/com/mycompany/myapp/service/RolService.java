package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.RolDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Rol}.
 */
public interface RolService {
    /**
     * Save a rol.
     *
     * @param rolDTO the entity to save.
     * @return the persisted entity.
     */
    RolDTO save(RolDTO rolDTO);

    /**
     * Updates a rol.
     *
     * @param rolDTO the entity to update.
     * @return the persisted entity.
     */
    RolDTO update(RolDTO rolDTO);

    /**
     * Partially updates a rol.
     *
     * @param rolDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RolDTO> partialUpdate(RolDTO rolDTO);

    /**
     * Get all the rols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RolDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rol.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RolDTO> findOne(Long id);

    /**
     * Delete the "id" rol.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
