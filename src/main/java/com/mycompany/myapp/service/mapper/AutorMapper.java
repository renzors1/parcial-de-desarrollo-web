package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Autor;
import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.service.dto.AutorDTO;
import com.mycompany.myapp.service.dto.LibroDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Autor} and its DTO {@link AutorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AutorMapper extends EntityMapper<AutorDTO, Autor> {
    @Mapping(target = "libroses", source = "libroses", qualifiedByName = "libroIdSet")
    AutorDTO toDto(Autor s);

    @Mapping(target = "libroses", ignore = true)
    @Mapping(target = "removeLibros", ignore = true)
    Autor toEntity(AutorDTO autorDTO);

    @Named("libroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibroDTO toDtoLibroId(Libro libro);

    @Named("libroIdSet")
    default Set<LibroDTO> toDtoLibroIdSet(Set<Libro> libro) {
        return libro.stream().map(this::toDtoLibroId).collect(Collectors.toSet());
    }
}
