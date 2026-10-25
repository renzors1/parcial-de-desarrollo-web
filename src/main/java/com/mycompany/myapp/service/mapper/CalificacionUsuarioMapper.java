package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.CalificacionUsuario;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.service.dto.CalificacionUsuarioDTO;
import com.mycompany.myapp.service.dto.UsuarioDTO;
import com.mycompany.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CalificacionUsuario} and its DTO {@link CalificacionUsuarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface CalificacionUsuarioMapper extends EntityMapper<CalificacionUsuarioDTO, CalificacionUsuario> {
    @Mapping(target = "clienteEvaluado", source = "clienteEvaluado", qualifiedByName = "usuarioId")
    @Mapping(target = "evaluadorTienda", source = "evaluadorTienda", qualifiedByName = "usuarioId")
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    CalificacionUsuarioDTO toDto(CalificacionUsuario s);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
