package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalificacionUsuarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalificacionUsuarioDTO.class);
        CalificacionUsuarioDTO calificacionUsuarioDTO1 = new CalificacionUsuarioDTO();
        calificacionUsuarioDTO1.setId(1L);
        CalificacionUsuarioDTO calificacionUsuarioDTO2 = new CalificacionUsuarioDTO();
        assertThat(calificacionUsuarioDTO1).isNotEqualTo(calificacionUsuarioDTO2);
        calificacionUsuarioDTO2.setId(calificacionUsuarioDTO1.getId());
        assertThat(calificacionUsuarioDTO1).isEqualTo(calificacionUsuarioDTO2);
        calificacionUsuarioDTO2.setId(2L);
        assertThat(calificacionUsuarioDTO1).isNotEqualTo(calificacionUsuarioDTO2);
        calificacionUsuarioDTO1.setId(null);
        assertThat(calificacionUsuarioDTO1).isNotEqualTo(calificacionUsuarioDTO2);
    }
}
