package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.EditorialAsserts.*;
import static com.mycompany.myapp.domain.EditorialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EditorialMapperTest {

    private EditorialMapper editorialMapper;

    @BeforeEach
    void setUp() {
        editorialMapper = new EditorialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEditorialSample1();
        var actual = editorialMapper.toEntity(editorialMapper.toDto(expected));
        assertEditorialAllPropertiesEquals(expected, actual);
    }
}
