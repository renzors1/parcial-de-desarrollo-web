package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LicenciaDigital;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LicenciaDigital entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicenciaDigitalRepository extends JpaRepository<LicenciaDigital, Long> {}
