package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RolTestSamples.*;
import static com.mycompany.myapp.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rol.class);
        Rol rol1 = getRolSample1();
        Rol rol2 = new Rol();
        assertThat(rol1).isNotEqualTo(rol2);

        rol2.setId(rol1.getId());
        assertThat(rol1).isEqualTo(rol2);

        rol2 = getRolSample2();
        assertThat(rol1).isNotEqualTo(rol2);
    }

    @Test
    void usuariosTest() {
        Rol rol = getRolRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        rol.addUsuarios(usuarioBack);
        assertThat(rol.getUsuarioses()).containsOnly(usuarioBack);
        assertThat(usuarioBack.getRol()).isEqualTo(rol);

        rol.removeUsuarios(usuarioBack);
        assertThat(rol.getUsuarioses()).doesNotContain(usuarioBack);
        assertThat(usuarioBack.getRol()).isNull();

        rol.usuarioses(new HashSet<>(Set.of(usuarioBack)));
        assertThat(rol.getUsuarioses()).containsOnly(usuarioBack);
        assertThat(usuarioBack.getRol()).isEqualTo(rol);

        rol.setUsuarioses(new HashSet<>());
        assertThat(rol.getUsuarioses()).doesNotContain(usuarioBack);
        assertThat(usuarioBack.getRol()).isNull();
    }
}
