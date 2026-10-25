package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LicenciaDigitalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LicenciaDigitalDTO.class);
        LicenciaDigitalDTO licenciaDigitalDTO1 = new LicenciaDigitalDTO();
        licenciaDigitalDTO1.setId(1L);
        LicenciaDigitalDTO licenciaDigitalDTO2 = new LicenciaDigitalDTO();
        assertThat(licenciaDigitalDTO1).isNotEqualTo(licenciaDigitalDTO2);
        licenciaDigitalDTO2.setId(licenciaDigitalDTO1.getId());
        assertThat(licenciaDigitalDTO1).isEqualTo(licenciaDigitalDTO2);
        licenciaDigitalDTO2.setId(2L);
        assertThat(licenciaDigitalDTO1).isNotEqualTo(licenciaDigitalDTO2);
        licenciaDigitalDTO1.setId(null);
        assertThat(licenciaDigitalDTO1).isNotEqualTo(licenciaDigitalDTO2);
    }
}
