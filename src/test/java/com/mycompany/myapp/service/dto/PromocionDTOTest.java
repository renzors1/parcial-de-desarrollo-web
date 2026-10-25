package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromocionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromocionDTO.class);
        PromocionDTO promocionDTO1 = new PromocionDTO();
        promocionDTO1.setId(1L);
        PromocionDTO promocionDTO2 = new PromocionDTO();
        assertThat(promocionDTO1).isNotEqualTo(promocionDTO2);
        promocionDTO2.setId(promocionDTO1.getId());
        assertThat(promocionDTO1).isEqualTo(promocionDTO2);
        promocionDTO2.setId(2L);
        assertThat(promocionDTO1).isNotEqualTo(promocionDTO2);
        promocionDTO1.setId(null);
        assertThat(promocionDTO1).isNotEqualTo(promocionDTO2);
    }
}
