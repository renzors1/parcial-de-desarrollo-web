package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AutorTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AutorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autor.class);
        Autor autor1 = getAutorSample1();
        Autor autor2 = new Autor();
        assertThat(autor1).isNotEqualTo(autor2);

        autor2.setId(autor1.getId());
        assertThat(autor1).isEqualTo(autor2);

        autor2 = getAutorSample2();
        assertThat(autor1).isNotEqualTo(autor2);
    }

    @Test
    void librosTest() {
        Autor autor = getAutorRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        autor.addLibros(libroBack);
        assertThat(autor.getLibroses()).containsOnly(libroBack);
        assertThat(libroBack.getAutoreses()).containsOnly(autor);

        autor.removeLibros(libroBack);
        assertThat(autor.getLibroses()).doesNotContain(libroBack);
        assertThat(libroBack.getAutoreses()).doesNotContain(autor);

        autor.libroses(new HashSet<>(Set.of(libroBack)));
        assertThat(autor.getLibroses()).containsOnly(libroBack);
        assertThat(libroBack.getAutoreses()).containsOnly(autor);

        autor.setLibroses(new HashSet<>());
        assertThat(autor.getLibroses()).doesNotContain(libroBack);
        assertThat(libroBack.getAutoreses()).doesNotContain(autor);
    }
}
