package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CalificacionUsuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CalificacionUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalificacionUsuarioRepository extends JpaRepository<CalificacionUsuario, Long> {}
