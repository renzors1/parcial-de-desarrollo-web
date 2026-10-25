package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EditorialTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EditorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Editorial.class);
        Editorial editorial1 = getEditorialSample1();
        Editorial editorial2 = new Editorial();
        assertThat(editorial1).isNotEqualTo(editorial2);

        editorial2.setId(editorial1.getId());
        assertThat(editorial1).isEqualTo(editorial2);

        editorial2 = getEditorialSample2();
        assertThat(editorial1).isNotEqualTo(editorial2);
    }

    @Test
    void librosTest() {
        Editorial editorial = getEditorialRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        editorial.addLibros(libroBack);
        assertThat(editorial.getLibroses()).containsOnly(libroBack);
        assertThat(libroBack.getEditorial()).isEqualTo(editorial);

        editorial.removeLibros(libroBack);
        assertThat(editorial.getLibroses()).doesNotContain(libroBack);
        assertThat(libroBack.getEditorial()).isNull();

        editorial.libroses(new HashSet<>(Set.of(libroBack)));
        assertThat(editorial.getLibroses()).containsOnly(libroBack);
        assertThat(libroBack.getEditorial()).isEqualTo(editorial);

        editorial.setLibroses(new HashSet<>());
        assertThat(editorial.getLibroses()).doesNotContain(libroBack);
        assertThat(libroBack.getEditorial()).isNull();
    }
}
