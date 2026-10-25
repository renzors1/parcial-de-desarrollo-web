package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.RolAsserts.*;
import static com.mycompany.myapp.domain.RolTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RolMapperTest {

    private RolMapper rolMapper;

    @BeforeEach
    void setUp() {
        rolMapper = new RolMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRolSample1();
        var actual = rolMapper.toEntity(rolMapper.toDto(expected));
        assertRolAllPropertiesEquals(expected, actual);
    }
}
