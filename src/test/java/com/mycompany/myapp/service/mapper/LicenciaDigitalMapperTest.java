package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.LicenciaDigitalAsserts.*;
import static com.mycompany.myapp.domain.LicenciaDigitalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LicenciaDigitalMapperTest {

    private LicenciaDigitalMapper licenciaDigitalMapper;

    @BeforeEach
    void setUp() {
        licenciaDigitalMapper = new LicenciaDigitalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLicenciaDigitalSample1();
        var actual = licenciaDigitalMapper.toEntity(licenciaDigitalMapper.toDto(expected));
        assertLicenciaDigitalAllPropertiesEquals(expected, actual);
    }
}
