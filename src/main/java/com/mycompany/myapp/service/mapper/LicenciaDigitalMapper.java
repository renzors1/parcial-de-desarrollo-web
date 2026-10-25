package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.DetalleVenta;
import com.mycompany.myapp.domain.Libro;
import com.mycompany.myapp.domain.LicenciaDigital;
import com.mycompany.myapp.service.dto.DetalleVentaDTO;
import com.mycompany.myapp.service.dto.LibroDTO;
import com.mycompany.myapp.service.dto.LicenciaDigitalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LicenciaDigital} and its DTO {@link LicenciaDigitalDTO}.
 */
@Mapper(componentModel = "spring")
public interface LicenciaDigitalMapper extends EntityMapper<LicenciaDigitalDTO, LicenciaDigital> {
    @Mapping(target = "libro", source = "libro", qualifiedByName = "libroId")
    @Mapping(target = "detalleVenta", source = "detalleVenta", qualifiedByName = "detalleVentaId")
    LicenciaDigitalDTO toDto(LicenciaDigital s);

    @Named("libroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibroDTO toDtoLibroId(Libro libro);

    @Named("detalleVentaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DetalleVentaDTO toDtoDetalleVentaId(DetalleVenta detalleVenta);
}
