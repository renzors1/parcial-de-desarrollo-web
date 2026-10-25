package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.service.dto.CategoriaDTO;
import com.mycompany.myapp.service.dto.LibroDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categoria} and its DTO {@link CategoriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaMapper extends EntityMapper<CategoriaDTO, Categoria> {
    @Mapping(target = "libroses", source = "libroses", qualifiedByName = "libroIdSet")
    CategoriaDTO toDto(Categoria s);

    @Mapping(target = "libroses", ignore = true)
    @Mapping(target = "removeLibros", ignore = true)
    Categoria toEntity(CategoriaDTO categoriaDTO);

    @Named("libroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibroDTO toDtoLibroId(Libro libro);

    @Named("libroIdSet")
    default Set<LibroDTO> toDtoLibroIdSet(Set<Libro> libro) {
        return libro.stream().map(this::toDtoLibroId).collect(Collectors.toSet());
    }
}
