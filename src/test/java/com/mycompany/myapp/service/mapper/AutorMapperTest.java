package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AutorAsserts.*;
import static com.mycompany.myapp.domain.AutorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutorMapperTest {

    private AutorMapper autorMapper;

    @BeforeEach
    void setUp() {
        autorMapper = new AutorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAutorSample1();
        var actual = autorMapper.toEntity(autorMapper.toDto(expected));
        assertAutorAllPropertiesEquals(expected, actual);
    }
}
