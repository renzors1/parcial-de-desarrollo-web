package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.UsuarioAsserts.*;
import static com.mycompany.myapp.domain.UsuarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioMapperTest {

    private UsuarioMapper usuarioMapper;

    @BeforeEach
    void setUp() {
        usuarioMapper = new UsuarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUsuarioSample1();
        var actual = usuarioMapper.toEntity(usuarioMapper.toDto(expected));
        assertUsuarioAllPropertiesEquals(expected, actual);
    }
}
