package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CalificacionUsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CalificacionUsuario getCalificacionUsuarioSample1() {
        return new CalificacionUsuario()
            .id(1L)
            .puntuacion(1)
            .comentario("comentario1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static CalificacionUsuario getCalificacionUsuarioSample2() {
        return new CalificacionUsuario()
            .id(2L)
            .puntuacion(2)
            .comentario("comentario2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static CalificacionUsuario getCalificacionUsuarioRandomSampleGenerator() {
        return new CalificacionUsuario()
            .id(longCount.incrementAndGet())
            .puntuacion(intCount.incrementAndGet())
            .comentario(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
