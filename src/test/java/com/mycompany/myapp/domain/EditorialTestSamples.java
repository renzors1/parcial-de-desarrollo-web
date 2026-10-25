package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EditorialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Editorial getEditorialSample1() {
        return new Editorial()
            .id(1L)
            .nombreEditorial("nombreEditorial1")
            .pais("pais1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Editorial getEditorialSample2() {
        return new Editorial()
            .id(2L)
            .nombreEditorial("nombreEditorial2")
            .pais("pais2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Editorial getEditorialRandomSampleGenerator() {
        return new Editorial()
            .id(longCount.incrementAndGet())
            .nombreEditorial(UUID.randomUUID().toString())
            .pais(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
