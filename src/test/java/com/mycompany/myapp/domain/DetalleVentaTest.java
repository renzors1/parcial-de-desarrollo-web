package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DetalleVentaTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static com.mycompany.myapp.domain.LicenciaDigitalTestSamples.*;
import static com.mycompany.myapp.domain.PromocionTestSamples.*;
import static com.mycompany.myapp.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DetalleVentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleVenta.class);
        DetalleVenta detalleVenta1 = getDetalleVentaSample1();
        DetalleVenta detalleVenta2 = new DetalleVenta();
        assertThat(detalleVenta1).isNotEqualTo(detalleVenta2);

        detalleVenta2.setId(detalleVenta1.getId());
        assertThat(detalleVenta1).isEqualTo(detalleVenta2);

        detalleVenta2 = getDetalleVentaSample2();
        assertThat(detalleVenta1).isNotEqualTo(detalleVenta2);
    }

    @Test
    void licenciasEntregadasTest() {
        DetalleVenta detalleVenta = getDetalleVentaRandomSampleGenerator();
        LicenciaDigital licenciaDigitalBack = getLicenciaDigitalRandomSampleGenerator();

        detalleVenta.addLicenciasEntregadas(licenciaDigitalBack);
        assertThat(detalleVenta.getLicenciasEntregadases()).containsOnly(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getDetalleVenta()).isEqualTo(detalleVenta);

        detalleVenta.removeLicenciasEntregadas(licenciaDigitalBack);
        assertThat(detalleVenta.getLicenciasEntregadases()).doesNotContain(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getDetalleVenta()).isNull();

        detalleVenta.licenciasEntregadases(new HashSet<>(Set.of(licenciaDigitalBack)));
        assertThat(detalleVenta.getLicenciasEntregadases()).containsOnly(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getDetalleVenta()).isEqualTo(detalleVenta);

        detalleVenta.setLicenciasEntregadases(new HashSet<>());
        assertThat(detalleVenta.getLicenciasEntregadases()).doesNotContain(licenciaDigitalBack);
        assertThat(licenciaDigitalBack.getDetalleVenta()).isNull();
    }

    @Test
    void ventaTest() {
        DetalleVenta detalleVenta = getDetalleVentaRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        detalleVenta.setVenta(ventaBack);
        assertThat(detalleVenta.getVenta()).isEqualTo(ventaBack);

        detalleVenta.venta(null);
        assertThat(detalleVenta.getVenta()).isNull();
    }

    @Test
    void libroTest() {
        DetalleVenta detalleVenta = getDetalleVentaRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        detalleVenta.setLibro(libroBack);
        assertThat(detalleVenta.getLibro()).isEqualTo(libroBack);

        detalleVenta.libro(null);
        assertThat(detalleVenta.getLibro()).isNull();
    }

    @Test
    void promocionAplicadaTest() {
        DetalleVenta detalleVenta = getDetalleVentaRandomSampleGenerator();
        Promocion promocionBack = getPromocionRandomSampleGenerator();

        detalleVenta.setPromocionAplicada(promocionBack);
        assertThat(detalleVenta.getPromocionAplicada()).isEqualTo(promocionBack);

        detalleVenta.promocionAplicada(null);
        assertThat(detalleVenta.getPromocionAplicada()).isNull();
    }
}
