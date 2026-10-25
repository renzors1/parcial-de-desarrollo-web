package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoriaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Categoria getCategoriaSample1() {
        return new Categoria()
            .id(1L)
            .nombreCategoria("nombreCategoria1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Categoria getCategoriaSample2() {
        return new Categoria()
            .id(2L)
            .nombreCategoria("nombreCategoria2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Categoria getCategoriaRandomSampleGenerator() {
        return new Categoria()
            .id(longCount.incrementAndGet())
            .nombreCategoria(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
