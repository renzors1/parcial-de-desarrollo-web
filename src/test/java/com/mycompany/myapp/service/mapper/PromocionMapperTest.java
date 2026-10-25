package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.PromocionAsserts.*;
import static com.mycompany.myapp.domain.PromocionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromocionMapperTest {

    private PromocionMapper promocionMapper;

    @BeforeEach
    void setUp() {
        promocionMapper = new PromocionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPromocionSample1();
        var actual = promocionMapper.toEntity(promocionMapper.toDto(expected));
        assertPromocionAllPropertiesEquals(expected, actual);
    }
}
