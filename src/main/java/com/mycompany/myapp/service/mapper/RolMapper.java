package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Rol;
import com.mycompany.myapp.service.dto.RolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rol} and its DTO {@link RolDTO}.
 */
@Mapper(componentModel = "spring")
public interface RolMapper extends EntityMapper<RolDTO, Rol> {}
