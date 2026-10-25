package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CalificacionUsuarioTestSamples.*;
import static com.mycompany.myapp.domain.RolTestSamples.*;
import static com.mycompany.myapp.domain.UsuarioTestSamples.*;
import static com.mycompany.myapp.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usuario.class);
        Usuario usuario1 = getUsuarioSample1();
        Usuario usuario2 = new Usuario();
        assertThat(usuario1).isNotEqualTo(usuario2);

        usuario2.setId(usuario1.getId());
        assertThat(usuario1).isEqualTo(usuario2);

        usuario2 = getUsuarioSample2();
        assertThat(usuario1).isNotEqualTo(usuario2);
    }

    @Test
    void comprasTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        usuario.addCompras(ventaBack);
        assertThat(usuario.getComprases()).containsOnly(ventaBack);
        assertThat(ventaBack.getCliente()).isEqualTo(usuario);

        usuario.removeCompras(ventaBack);
        assertThat(usuario.getComprases()).doesNotContain(ventaBack);
        assertThat(ventaBack.getCliente()).isNull();

        usuario.comprases(new HashSet<>(Set.of(ventaBack)));
        assertThat(usuario.getComprases()).containsOnly(ventaBack);
        assertThat(ventaBack.getCliente()).isEqualTo(usuario);

        usuario.setComprases(new HashSet<>());
        assertThat(usuario.getComprases()).doesNotContain(ventaBack);
        assertThat(ventaBack.getCliente()).isNull();
    }

    @Test
    void calificacionesRecibidasTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        CalificacionUsuario calificacionUsuarioBack = getCalificacionUsuarioRandomSampleGenerator();

        usuario.addCalificacionesRecibidas(calificacionUsuarioBack);
        assertThat(usuario.getCalificacionesRecibidases()).containsOnly(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getClienteEvaluado()).isEqualTo(usuario);

        usuario.removeCalificacionesRecibidas(calificacionUsuarioBack);
        assertThat(usuario.getCalificacionesRecibidases()).doesNotContain(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getClienteEvaluado()).isNull();

        usuario.calificacionesRecibidases(new HashSet<>(Set.of(calificacionUsuarioBack)));
        assertThat(usuario.getCalificacionesRecibidases()).containsOnly(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getClienteEvaluado()).isEqualTo(usuario);

        usuario.setCalificacionesRecibidases(new HashSet<>());
        assertThat(usuario.getCalificacionesRecibidases()).doesNotContain(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getClienteEvaluado()).isNull();
    }

    @Test
    void calificacionesEmitidasTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        CalificacionUsuario calificacionUsuarioBack = getCalificacionUsuarioRandomSampleGenerator();

        usuario.addCalificacionesEmitidas(calificacionUsuarioBack);
        assertThat(usuario.getCalificacionesEmitidases()).containsOnly(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getEvaluadorTienda()).isEqualTo(usuario);

        usuario.removeCalificacionesEmitidas(calificacionUsuarioBack);
        assertThat(usuario.getCalificacionesEmitidases()).doesNotContain(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getEvaluadorTienda()).isNull();

        usuario.calificacionesEmitidases(new HashSet<>(Set.of(calificacionUsuarioBack)));
        assertThat(usuario.getCalificacionesEmitidases()).containsOnly(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getEvaluadorTienda()).isEqualTo(usuario);

        usuario.setCalificacionesEmitidases(new HashSet<>());
        assertThat(usuario.getCalificacionesEmitidases()).doesNotContain(calificacionUsuarioBack);
        assertThat(calificacionUsuarioBack.getEvaluadorTienda()).isNull();
    }

    @Test
    void rolTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Rol rolBack = getRolRandomSampleGenerator();

        usuario.setRol(rolBack);
        assertThat(usuario.getRol()).isEqualTo(rolBack);

        usuario.rol(null);
        assertThat(usuario.getRol()).isNull();
    }
}
