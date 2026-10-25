package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LibroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Libro getLibroSample1() {
        return new Libro()
            .id(1L)
            .isbn("isbn1")
            .titulo("titulo1")
            .urlPortada("urlPortada1")
            .idioma("idioma1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Libro getLibroSample2() {
        return new Libro()
            .id(2L)
            .isbn("isbn2")
            .titulo("titulo2")
            .urlPortada("urlPortada2")
            .idioma("idioma2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Libro getLibroRandomSampleGenerator() {
        return new Libro()
            .id(longCount.incrementAndGet())
            .isbn(UUID.randomUUID().toString())
            .titulo(UUID.randomUUID().toString())
            .urlPortada(UUID.randomUUID().toString())
            .idioma(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
