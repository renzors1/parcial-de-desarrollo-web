package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.LibroAsserts.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibroMapperTest {

    private LibroMapper libroMapper;

    @BeforeEach
    void setUp() {
        libroMapper = new LibroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLibroSample1();
        var actual = libroMapper.toEntity(libroMapper.toDto(expected));
        assertLibroAllPropertiesEquals(expected, actual);
    }
}
