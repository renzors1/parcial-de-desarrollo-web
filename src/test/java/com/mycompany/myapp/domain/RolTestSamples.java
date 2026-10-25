package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RolTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Rol getRolSample1() {
        return new Rol()
            .id(1L)
            .codigoIdentificador(1)
            .nombreRol("nombreRol1")
            .descripcion("descripcion1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Rol getRolSample2() {
        return new Rol()
            .id(2L)
            .codigoIdentificador(2)
            .nombreRol("nombreRol2")
            .descripcion("descripcion2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Rol getRolRandomSampleGenerator() {
        return new Rol()
            .id(longCount.incrementAndGet())
            .codigoIdentificador(intCount.incrementAndGet())
            .nombreRol(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
