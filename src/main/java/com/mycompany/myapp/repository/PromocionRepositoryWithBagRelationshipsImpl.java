package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Promocion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PromocionRepositoryWithBagRelationshipsImpl implements PromocionRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PROMOCIONS_PARAMETER = "promocions";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Promocion> fetchBagRelationships(Optional<Promocion> promocion) {
        return promocion.map(this::fetchLibroses);
    }

    @Override
    public Page<Promocion> fetchBagRelationships(Page<Promocion> promocions) {
        return new PageImpl<>(fetchBagRelationships(promocions.getContent()), promocions.getPageable(), promocions.getTotalElements());
    }

    @Override
    public List<Promocion> fetchBagRelationships(List<Promocion> promocions) {
        return Optional.of(promocions).map(this::fetchLibroses).orElse(List.of());
    }

    Promocion fetchLibroses(Promocion result) {
        return entityManager
            .createQuery(
                "select promocion from Promocion promocion left join fetch promocion.libroses where promocion.id = :id",
                Promocion.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Promocion> fetchLibroses(List<Promocion> promocions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, promocions.size()).forEach(index -> order.put(promocions.get(index).getId(), index));
        List<Promocion> result = entityManager
            .createQuery(
                "select promocion from Promocion promocion left join fetch promocion.libroses where promocion in :promocions",
                Promocion.class
            )
            .setParameter(PROMOCIONS_PARAMETER, promocions)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
