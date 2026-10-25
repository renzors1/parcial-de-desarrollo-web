package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutorDTO.class);
        AutorDTO autorDTO1 = new AutorDTO();
        autorDTO1.setId(1L);
        AutorDTO autorDTO2 = new AutorDTO();
        assertThat(autorDTO1).isNotEqualTo(autorDTO2);
        autorDTO2.setId(autorDTO1.getId());
        assertThat(autorDTO1).isEqualTo(autorDTO2);
        autorDTO2.setId(2L);
        assertThat(autorDTO1).isNotEqualTo(autorDTO2);
        autorDTO1.setId(null);
        assertThat(autorDTO1).isNotEqualTo(autorDTO2);
    }
}
