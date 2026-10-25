package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.DetalleVentaAsserts.*;
import static com.mycompany.myapp.domain.DetalleVentaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetalleVentaMapperTest {

    private DetalleVentaMapper detalleVentaMapper;

    @BeforeEach
    void setUp() {
        detalleVentaMapper = new DetalleVentaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDetalleVentaSample1();
        var actual = detalleVentaMapper.toEntity(detalleVentaMapper.toDto(expected));
        assertDetalleVentaAllPropertiesEquals(expected, actual);
    }
}
