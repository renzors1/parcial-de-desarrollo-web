package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CalificacionUsuarioTestSamples.*;
import static com.mycompany.myapp.domain.DetalleVentaTestSamples.*;
import static com.mycompany.myapp.domain.UsuarioTestSamples.*;
import static com.mycompany.myapp.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = getVentaSample1();
        Venta venta2 = new Venta();
        assertThat(venta1).isNotEqualTo(venta2);

        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);

        venta2 = getVentaSample2();
        assertThat(venta1).isNotEqualTo(venta2);
    }

    @Test
    void calificacionesClienteTest() {
        Venta venta = getVentaRandomSampleGenerator();
        CalificacionUsuario calificacionUsuarioBack = getCalificacionUsuarioRandomSampleGenerator();

        venta.addCalificacionesCliente(calificacionUsuarioBack);
        assertThat(venta.getCalificacionesClientes()).containsOnly(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getVenta()).isEqualTo(venta);

        venta.removeCalificacionesCliente(calificacionUsuarioBack);
        assertThat(venta.getCalificacionesClientes()).doesNotContain(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getVenta()).isNull();

        venta.calificacionesClientes(new HashSet<>(Set.of(calificacionUsuarioBack)));
        assertThat(venta.getCalificacionesClientes()).containsOnly(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getVenta()).isEqualTo(venta);

        venta.setCalificacionesClientes(new HashSet<>());
        assertThat(venta.getCalificacionesClientes()).doesNotContain(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getVenta()).isNull();
    }

    @Test
    void detallesTest() {
        Venta venta = getVentaRandomSampleGenerator();
        DetalleVenta detalleVentaBack = getDetalleVentaRandomSampleGenerator();

        venta.addDetalles(detalleVentaBack);
        assertThat(venta.getDetalleses()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isEqualTo(venta);

        venta.removeDetalles(detalleVentaBack);
        assertThat(venta.getDetalleses()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isNull();

        venta.detalleses(new HashSet<>(Set.of(detalleVentaBack)));
        assertThat(venta.getDetalleses()).containsOnly(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isEqualTo(venta);

        venta.setDetalleses(new HashSet<>());
        assertThat(venta.getDetalleses()).doesNotContain(detalleVentaBack);
        assertThat(detalleVentaBack.getVenta()).isNull();
    }

    @Test
    void clienteTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        venta.setCliente(usuarioBack);
        assertThat(venta.getCliente()).isEqualTo(usuarioBack);

        venta.cliente(null);
        assertThat(venta.getCliente()).isNull();
    }
}
