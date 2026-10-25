package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Libro;
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
public class LibroRepositoryWithBagRelationshipsImpl implements LibroRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String LIBROS_PARAMETER = "libros";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Libro> fetchBagRelationships(Optional<Libro> libro) {
        return libro.map(this::fetchCategoriases).map(this::fetchAutoreses);
    }

    @Override
    public Page<Libro> fetchBagRelationships(Page<Libro> libros) {
        return new PageImpl<>(fetchBagRelationships(libros.getContent()), libros.getPageable(), libros.getTotalElements());
    }

    @Override
    public List<Libro> fetchBagRelationships(List<Libro> libros) {
        return Optional.of(libros).map(this::fetchCategoriases).map(this::fetchAutoreses).orElse(List.of());
    }

    Libro fetchCategoriases(Libro result) {
        return entityManager
            .createQuery("select libro from Libro libro left join fetch libro.categoriases where libro.id = :id", Libro.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Libro> fetchCategoriases(List<Libro> libros) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, libros.size()).forEach(index -> order.put(libros.get(index).getId(), index));
        List<Libro> result = entityManager
            .createQuery("select libro from Libro libro left join fetch libro.categoriases where libro in :libros", Libro.class)
            .setParameter(LIBROS_PARAMETER, libros)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Libro fetchAutoreses(Libro result) {
        return entityManager
            .createQuery("select libro from Libro libro left join fetch libro.autoreses where libro.id = :id", Libro.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Libro> fetchAutoreses(List<Libro> libros) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, libros.size()).forEach(index -> order.put(libros.get(index).getId(), index));
        List<Libro> result = entityManager
            .createQuery("select libro from Libro libro left join fetch libro.autoreses where libro in :libros", Libro.class)
            .setParameter(LIBROS_PARAMETER, libros)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
