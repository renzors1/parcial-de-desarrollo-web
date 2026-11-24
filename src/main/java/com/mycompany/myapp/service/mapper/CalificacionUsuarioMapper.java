package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.CalificacionUsuario;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.service.dto.CalificacionUsuarioDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CalificacionUsuario} and its DTO {@link CalificacionUsuarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface CalificacionUsuarioMapper extends EntityMapper<CalificacionUsuarioDTO, CalificacionUsuario> {
    @Mapping(target = "clienteEvaluado", source = "clienteEvaluado", qualifiedByName = "userLogin")
    @Mapping(target = "evaluadorTienda", source = "evaluadorTienda", qualifiedByName = "userLogin")
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    CalificacionUsuarioDTO toDto(CalificacionUsuario s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
