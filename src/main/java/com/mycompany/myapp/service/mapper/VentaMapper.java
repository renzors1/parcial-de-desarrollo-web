package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.service.dto.UsuarioDTO;
import com.mycompany.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "usuarioId")
    VentaDTO toDto(Venta s);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
