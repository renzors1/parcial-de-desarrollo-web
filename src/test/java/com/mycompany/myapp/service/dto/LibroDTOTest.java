package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LibroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LibroDTO.class);
        LibroDTO libroDTO1 = new LibroDTO();
        libroDTO1.setId(1L);
        LibroDTO libroDTO2 = new LibroDTO();
        assertThat(libroDTO1).isNotEqualTo(libroDTO2);
        libroDTO2.setId(libroDTO1.getId());
        assertThat(libroDTO1).isEqualTo(libroDTO2);
        libroDTO2.setId(2L);
        assertThat(libroDTO1).isNotEqualTo(libroDTO2);
        libroDTO1.setId(null);
        assertThat(libroDTO1).isNotEqualTo(libroDTO2);
    }
}
