package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Editorial;
import com.mycompany.myapp.service.dto.EditorialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Editorial} and its DTO {@link EditorialDTO}.
 */
@Mapper(componentModel = "spring")
public interface EditorialMapper extends EntityMapper<EditorialDTO, Editorial> {}
