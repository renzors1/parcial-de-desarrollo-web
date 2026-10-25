package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EditorialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EditorialDTO.class);
        EditorialDTO editorialDTO1 = new EditorialDTO();
        editorialDTO1.setId(1L);
        EditorialDTO editorialDTO2 = new EditorialDTO();
        assertThat(editorialDTO1).isNotEqualTo(editorialDTO2);
        editorialDTO2.setId(editorialDTO1.getId());
        assertThat(editorialDTO1).isEqualTo(editorialDTO2);
        editorialDTO2.setId(2L);
        assertThat(editorialDTO1).isNotEqualTo(editorialDTO2);
        editorialDTO1.setId(null);
        assertThat(editorialDTO1).isNotEqualTo(editorialDTO2);
    }
}
