package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CalificacionUsuarioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CalificacionUsuario}.
 */
public interface CalificacionUsuarioService {
    /**
     * Save a calificacionUsuario.
     *
     * @param calificacionUsuarioDTO the entity to save.
     * @return the persisted entity.
     */
    CalificacionUsuarioDTO save(CalificacionUsuarioDTO calificacionUsuarioDTO);

    /**
     * Updates a calificacionUsuario.
     *
     * @param calificacionUsuarioDTO the entity to update.
     * @return the persisted entity.
     */
    CalificacionUsuarioDTO update(CalificacionUsuarioDTO calificacionUsuarioDTO);

    /**
     * Partially updates a calificacionUsuario.
     *
     * @param calificacionUsuarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CalificacionUsuarioDTO> partialUpdate(CalificacionUsuarioDTO calificacionUsuarioDTO);

    /**
     * Get all the calificacionUsuarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalificacionUsuarioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" calificacionUsuario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CalificacionUsuarioDTO> findOne(Long id);

    /**
     * Delete the "id" calificacionUsuario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
