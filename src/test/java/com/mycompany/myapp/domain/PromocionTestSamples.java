package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PromocionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Promocion getPromocionSample1() {
        return new Promocion()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .cantidadDefinida(1)
            .cantidadDisponible(1)
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Promocion getPromocionSample2() {
        return new Promocion()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .cantidadDefinida(2)
            .cantidadDisponible(2)
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Promocion getPromocionRandomSampleGenerator() {
        return new Promocion()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .cantidadDefinida(intCount.incrementAndGet())
            .cantidadDisponible(intCount.incrementAndGet())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
