package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Autor;
import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.domain.Editorial;
import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.domain.Promocion;
import com.mycompany.myapp.service.dto.AutorDTO;
import com.mycompany.myapp.service.dto.CategoriaDTO;
import com.mycompany.myapp.service.dto.EditorialDTO;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.service.dto.PromocionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Libro} and its DTO {@link LibroDTO}.
 */
@Mapper(componentModel = "spring")
public interface LibroMapper extends EntityMapper<LibroDTO, Libro> {
    @Mapping(target = "categoriases", source = "categoriases", qualifiedByName = "categoriaIdSet")
    @Mapping(target = "autoreses", source = "autoreses", qualifiedByName = "autorIdSet")
    @Mapping(target = "editorial", source = "editorial", qualifiedByName = "editorialId")
    @Mapping(target = "promocioneses", source = "promocioneses", qualifiedByName = "promocionIdSet")
    LibroDTO toDto(Libro s);

    @Mapping(target = "removeCategorias", ignore = true)
    @Mapping(target = "removeAutores", ignore = true)
    @Mapping(target = "promocioneses", ignore = true)
    @Mapping(target = "removePromociones", ignore = true)
    Libro toEntity(LibroDTO libroDTO);

    @Named("categoriaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoriaDTO toDtoCategoriaId(Categoria categoria);

    @Named("categoriaIdSet")
    default Set<CategoriaDTO> toDtoCategoriaIdSet(Set<Categoria> categoria) {
        return categoria.stream().map(this::toDtoCategoriaId).collect(Collectors.toSet());
    }

    @Named("autorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AutorDTO toDtoAutorId(Autor autor);

    @Named("autorIdSet")
    default Set<AutorDTO> toDtoAutorIdSet(Set<Autor> autor) {
        return autor.stream().map(this::toDtoAutorId).collect(Collectors.toSet());
    }

    @Named("editorialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EditorialDTO toDtoEditorialId(Editorial editorial);

    @Named("promocionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PromocionDTO toDtoPromocionId(Promocion promocion);

    @Named("promocionIdSet")
    default Set<PromocionDTO> toDtoPromocionIdSet(Set<Promocion> promocion) {
        return promocion.stream().map(this::toDtoPromocionId).collect(Collectors.toSet());
    }
}
