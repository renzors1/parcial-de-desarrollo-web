package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DetalleVentaTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static com.mycompany.myapp.domain.PromocionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PromocionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promocion.class);
        Promocion promocion1 = getPromocionSample1();
        Promocion promocion2 = new Promocion();
        assertThat(promocion1).isNotEqualTo(promocion2);

        promocion2.setId(promocion1.getId());
        assertThat(promocion1).isEqualTo(promocion2);

        promocion2 = getPromocionSample2();
        assertThat(promocion1).isNotEqualTo(promocion2);
    }

    @Test
    void detallesVentaAplicadosTest() {
        Promocion promocion = getPromocionRandomSampleGenerator();
        DetalleVenta detalleVentaBack = getDetalleVentaRandomSampleGenerator();

        promocion.addDetallesVentaAplicados(detalleVentaBack);
        assertThat(promocion.getDetallesVentaAplicadoses()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getPromocionAplicada()).isEqualTo(promocion);

        promocion.removeDetallesVentaAplicados(detalleVentaBack);
        assertThat(promocion.getDetallesVentaAplicadoses()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getPromocionAplicada()).isNull();

        promocion.detallesVentaAplicadoses(new HashSet<>(Set.of(detalleVentaBack)));
        assertThat(promocion.getDetallesVentaAplicadoses()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getPromocionAplicada()).isEqualTo(promocion);

        promocion.setDetallesVentaAplicadoses(new HashSet<>());
        assertThat(promocion.getDetallesVentaAplicadoses()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getPromocionAplicada()).isNull();
    }

    @Test
    void librosTest() {
        Promocion promocion = getPromocionRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        promocion.addLibros(libroBack);
        assertThat(promocion.getLibroses()).containsOnly(libroBack);

        promocion.removeLibros(libroBack);
        assertThat(promocion.getLibroses()).doesNotContain(libroBack);

        promocion.libroses(new HashSet<>(Set.of(libroBack)));
        assertThat(promocion.getLibroses()).containsOnly(libroBack);

        promocion.setLibroses(new HashSet<>());
        assertThat(promocion.getLibroses()).doesNotContain(libroBack);
    }
}
