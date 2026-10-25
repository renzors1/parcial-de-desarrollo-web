package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.service.dto.RolDTO;
import com.mycompany.myapp.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "rol", source = "rol", qualifiedByName = "rolId")
    UsuarioDTO toDto(Usuario s);

    @Named("rolId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RolDTO toDtoRolId(Rol rol);
}
