package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DetalleVentaTestSamples.*;
import static com.mycompany.myapp.domain.LibroTestSamples.*;
import static com.mycompany.myapp.domain.LicenciaDigitalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LicenciaDigitalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LicenciaDigital.class);
        LicenciaDigital licenciaDigital1 = getLicenciaDigitalSample1();
        LicenciaDigital licenciaDigital2 = new LicenciaDigital();
        assertThat(licenciaDigital1).isNotEqualTo(licenciaDigital2);

        licenciaDigital2.setId(licenciaDigital1.getId());
        assertThat(licenciaDigital1).isEqualTo(licenciaDigital2);

        licenciaDigital2 = getLicenciaDigitalSample2();
        assertThat(licenciaDigital1).isNotEqualTo(licenciaDigital2);
    }

    @Test
    void libroTest() {
        LicenciaDigital licenciaDigital = getLicenciaDigitalRandomSampleGenerator();
        Libro libroBack = getLibroRandomSampleGenerator();

        licenciaDigital.setLibro(libroBack);
        assertThat(licenciaDigital.getLibro()).isEqualTo(libroBack);

        licenciaDigital.libro(null);
        assertThat(licenciaDigital.getLibro()).isNull();
    }

    @Test
    void detalleVentaTest() {
        LicenciaDigital licenciaDigital = getLicenciaDigitalRandomSampleGenerator();
        DetalleVenta detalleVentaBack = getDetalleVentaRandomSampleGenerator();

        licenciaDigital.setDetalleVenta(detalleVentaBack);
        assertThat(licenciaDigital.getDetalleVenta()).isEqualTo(detalleVentaBack);

        licenciaDigital.detalleVenta(null);
        assertThat(licenciaDigital.getDetalleVenta()).isNull();
    }
}
