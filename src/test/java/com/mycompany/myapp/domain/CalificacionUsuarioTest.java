package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CalificacionUsuarioTestSamples.*;
import static com.mycompany.myapp.domain.UsuarioTestSamples.*;
import static com.mycompany.myapp.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalificacionUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalificacionUsuario.class);
        CalificacionUsuario calificacionUsuario1 = getCalificacionUsuarioSample1();
        CalificacionUsuario calificacionUsuario2 = new CalificacionUsuario();
        assertThat(calificacionUsuario1).isNotEqualTo(calificacionUsuario2);

        calificacionUsuario2.setId(calificacionUsuario1.getId());
        assertThat(calificacionUsuario1).isEqualTo(calificacionUsuario2);

        calificacionUsuario2 = getCalificacionUsuarioSample2();
        assertThat(calificacionUsuario1).isNotEqualTo(calificacionUsuario2);
    }

    @Test
    void clienteEvaluadoTest() {
        CalificacionUsuario calificacionUsuario = getCalificacionUsuarioRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        calificacionUsuario.setClienteEvaluado(usuarioBack);
        assertThat(calificacionUsuario.getClienteEvaluado()).isEqualTo(usuarioBack);

        calificacionUsuario.clienteEvaluado(null);
        assertThat(calificacionUsuario.getClienteEvaluado()).isNull();
    }

    @Test
    void evaluadorTiendaTest() {
        CalificacionUsuario calificacionUsuario = getCalificacionUsuarioRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        calificacionUsuario.setEvaluadorTienda(usuarioBack);
        assertThat(calificacionUsuario.getEvaluadorTienda()).isEqualTo(usuarioBack);

        calificacionUsuario.evaluadorTienda(null);
        assertThat(calificacionUsuario.getEvaluadorTienda()).isNull();
    }

    @Test
    void ventaTest() {
        CalificacionUsuario calificacionUsuario = getCalificacionUsuarioRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        calificacionUsuario.setVenta(ventaBack);
        assertThat(calificacionUsuario.getVenta()).isEqualTo(ventaBack);

        calificacionUsuario.venta(null);
        assertThat(calificacionUsuario.getVenta()).isNull();
    }
}
