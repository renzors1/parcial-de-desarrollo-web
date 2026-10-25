package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Promocion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PromocionRepositoryWithBagRelationships {
    Optional<Promocion> fetchBagRelationships(Optional<Promocion> promocion);

    List<Promocion> fetchBagRelationships(List<Promocion> promocions);

    Page<Promocion> fetchBagRelationships(Page<Promocion> promocions);
}
