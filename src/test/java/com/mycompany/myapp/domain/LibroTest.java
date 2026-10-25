package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AutorTestSamples.*;
import static com.mycompany.myapp.domain.CategoriaTestSamples.*;
import static com.mycompany.myapp.domain.DetalleVentaTestSamples.*;
import static com.mycompany.myapp.domain.EditorialTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static com.mycompany.myapp.domain.LicenciaDigitalTestSamples.*;
import static com.mycompany.myapp.domain.PromocionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LibroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Libro.class);
        Libro libro1 = getLibroSample1();
        Libro libro2 = new Libro();
        assertThat(libro1).isNotEqualTo(libro2);

        libro2.setId(libro1.getId());
        assertThat(libro1).isEqualTo(libro2);

        libro2 = getLibroSample2();
        assertThat(libro1).isNotEqualTo(libro2);
    }

    @Test
    void detallesVentaTest() {
        Libro libro = getLibroRandomSampleGenerator();
        DetalleVenta detalleVentaBack = getDetalleVentaRandomSampleGenerator();

        libro.addDetallesVenta(detalleVentaBack);
        assertThat(libro.getDetallesVentas()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getLibro()).isEqualTo(libro);

        libro.removeDetallesVenta(detalleVentaBack);
        assertThat(libro.getDetallesVentas()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getLibro()).isNull();

        libro.detallesVentas(new HashSet<>(Set.of(detalleVentaBack)));
        assertThat(libro.getDetallesVentas()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getLibro()).isEqualTo(libro);

        libro.setDetallesVentas(new HashSet<>());
        assertThat(libro.getDetallesVentas()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getLibro()).isNull();
    }

    @Test
    void licenciasTest() {
        Libro libro = getLibroRandomSampleGenerator();
        LicenciaDigital licenciaDigitalBack = getLicenciaDigitalRandomSampleGenerator();

        libro.addLicencias(licenciaDigitalBack);
        assertThat(libro.getLicenciases()).containsOnly(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getLibro()).isEqualTo(libro);

        libro.removeLicencias(licenciaDigitalBack);
        assertThat(libro.getLicenciases()).doesNotContain(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getLibro()).isNull();

        libro.licenciases(new HashSet<>(Set.of(licenciaDigitalBack)));
        assertThat(libro.getLicenciases()).containsOnly(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getLibro()).isEqualTo(libro);

        libro.setLicenciases(new HashSet<>());
        assertThat(libro.getLicenciases()).doesNotContain(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getLibro()).isNull();
    }

    @Test
    void categoriasTest() {
        Libro libro = getLibroRandomSampleGenerator();
        Categoria categoriaBack = getCategoriaRandomSampleGenerator();

        libro.addCategorias(categoriaBack);
        assertThat(libro.getCategoriases()).containsOnly(categoriaBack);

        libro.removeCategorias(categoriaBack);
        assertThat(libro.getCategoriases()).doesNotContain(categoriaBack);

        libro.categoriases(new HashSet<>(Set.of(categoriaBack)));
        assertThat(libro.getCategoriases()).containsOnly(categoriaBack);

        libro.setCategoriases(new HashSet<>());
        assertThat(libro.getCategoriases()).doesNotContain(categoriaBack);
    }

    @Test
    void autoresTest() {
        Libro libro = getLibroRandomSampleGenerator();
        Autor autorBack = getAutorRandomSampleGenerator();

        libro.addAutores(autorBack);
        assertThat(libro.getAutoreses()).containsOnly(autorBack);

        libro.removeAutores(autorBack);
        assertThat(libro.getAutoreses()).doesNotContain(autorBack);

        libro.autoreses(new HashSet<>(Set.of(autorBack)));
        assertThat(libro.getAutoreses()).containsOnly(autorBack);

        libro.setAutoreses(new HashSet<>());
        assertThat(libro.getAutoreses()).doesNotContain(autorBack);
    }

    @Test
    void editorialTest() {
        Libro libro = getLibroRandomSampleGenerator();
        Editorial editorialBack = getEditorialRandomSampleGenerator();

        libro.setEditorial(editorialBack);
        assertThat(libro.getEditorial()).isEqualTo(editorialBack);

        libro.editorial(null);
        assertThat(libro.getEditorial()).isNull();
    }

    @Test
    void promocionesTest() {
        Libro libro = getLibroRandomSampleGenerator();
        Promocion promocionBack = getPromocionRandomSampleGenerator();

        libro.addPromociones(promocionBack);
        assertThat(libro.getPromocioneses()).containsOnly(promocionBack);
        assertThat(promocionBack.getLibroses()).containsOnly(libro);

        libro.removePromociones(promocionBack);
        assertThat(libro.getPromocioneses()).doesNotContain(promocionBack);
        assertThat(promocionBack.getLibroses()).doesNotContain(libro);

        libro.promocioneses(new HashSet<>(Set.of(promocionBack)));
        assertThat(libro.getPromocioneses()).containsOnly(promocionBack);
        assertThat(promocionBack.getLibroses()).containsOnly(libro);

        libro.setPromocioneses(new HashSet<>());
        assertThat(libro.getPromocioneses()).doesNotContain(promocionBack);
        assertThat(promocionBack.getLibroses()).doesNotContain(libro);
    }
}
