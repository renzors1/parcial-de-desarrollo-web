package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AutorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Autor getAutorSample1() {
        return new Autor()
            .id(1L)
            .nombres("nombres1")
            .apellidos("apellidos1")
            .nacionalidad("nacionalidad1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Autor getAutorSample2() {
        return new Autor()
            .id(2L)
            .nombres("nombres2")
            .apellidos("apellidos2")
            .nacionalidad("nacionalidad2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Autor getAutorRandomSampleGenerator() {
        return new Autor()
            .id(longCount.incrementAndGet())
            .nombres(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .nacionalidad(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
