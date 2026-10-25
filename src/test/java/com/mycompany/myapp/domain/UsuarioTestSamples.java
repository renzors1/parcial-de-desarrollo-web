package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Usuario getUsuarioSample1() {
        return new Usuario()
            .id(1L)
            .nombre("nombre1")
            .apellido("apellido1")
            .email("email1")
            .contrasena("contrasena1")
            .direccion("direccion1")
            .celular("celular1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Usuario getUsuarioSample2() {
        return new Usuario()
            .id(2L)
            .nombre("nombre2")
            .apellido("apellido2")
            .email("email2")
            .contrasena("contrasena2")
            .direccion("direccion2")
            .celular("celular2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Usuario getUsuarioRandomSampleGenerator() {
        return new Usuario()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .contrasena(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .celular(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
