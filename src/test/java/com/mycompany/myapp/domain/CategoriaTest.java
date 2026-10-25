package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoriaTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categoria.class);
        Categoria categoria1 = getCategoriaSample1();
        Categoria categoria2 = new Categoria();
        assertThat(categoria1).isNotEqualTo(categoria2);

        categoria2.setId(categoria1.getId());
        assertThat(categoria1).isEqualTo(categoria2);

        categoria2 = getCategoriaSample2();
        assertThat(categoria1).isNotEqualTo(categoria2);
    }

    @Test
    void librosTest() {
        Categoria categoria = getCategoriaRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        categoria.addLibros(libroBack);
        assertThat(categoria.getLibroses()).containsOnly(libroBack);
        assertThat(libroBack.getCategoriases()).containsOnly(categoria);

        categoria.removeLibros(libroBack);
        assertThat(categoria.getLibroses()).doesNotContain(libroBack);
        assertThat(libroBack.getCategoriases()).doesNotContain(categoria);

        categoria.libroses(new HashSet<>(Set.of(libroBack)));
        assertThat(categoria.getLibroses()).containsOnly(libroBack);
        assertThat(libroBack.getCategoriases()).containsOnly(categoria);

        categoria.setLibroses(new HashSet<>());
        assertThat(categoria.getLibroses()).doesNotContain(libroBack);
        assertThat(libroBack.getCategoriases()).doesNotContain(categoria);
    }
}
