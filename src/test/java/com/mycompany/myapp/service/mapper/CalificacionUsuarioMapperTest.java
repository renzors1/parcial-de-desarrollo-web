package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.CalificacionUsuarioAsserts.*;
import static com.mycompany.myapp.domain.CalificacionUsuarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalificacionUsuarioMapperTest {

    private CalificacionUsuarioMapper calificacionUsuarioMapper;

    @BeforeEach
    void setUp() {
        calificacionUsuarioMapper = new CalificacionUsuarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCalificacionUsuarioSample1();
        var actual = calificacionUsuarioMapper.toEntity(calificacionUsuarioMapper.toDto(expected));
        assertCalificacionUsuarioAllPropertiesEquals(expected, actual);
    }
}
