package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.DetalleVenta;
import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.domain.Promocion;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.service.dto.DetalleVentaDTO;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.service.dto.PromocionDTO;
import com.mycompany.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DetalleVenta} and its DTO {@link DetalleVentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DetalleVentaMapper extends EntityMapper<DetalleVentaDTO, DetalleVenta> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    @Mapping(target = "libro", source = "libro", qualifiedByName = "libroId")
    @Mapping(target = "promocionAplicada", source = "promocionAplicada", qualifiedByName = "promocionId")
    DetalleVentaDTO toDto(DetalleVenta s);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);

    @Named("libroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibroDTO toDtoLibroId(Libro libro);

    @Named("promocionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PromocionDTO toDtoPromocionId(Promocion promocion);
}
