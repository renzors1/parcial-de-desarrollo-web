package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.domain.Promocion;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.service.dto.PromocionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Promocion} and its DTO {@link PromocionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromocionMapper extends EntityMapper<PromocionDTO, Promocion> {
    @Mapping(target = "libroses", source = "libroses", qualifiedByName = "libroIdSet")
    PromocionDTO toDto(Promocion s);

    @Mapping(target = "removeLibros", ignore = true)
    Promocion toEntity(PromocionDTO promocionDTO);

    @Named("libroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibroDTO toDtoLibroId(Libro libro);

    @Named("libroIdSet")
    default Set<LibroDTO> toDtoLibroIdSet(Set<Libro> libro) {
        return libro.stream().map(this::toDtoLibroId).collect(Collectors.toSet());
    }
}
