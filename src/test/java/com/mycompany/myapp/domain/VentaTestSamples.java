package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VentaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Venta getVentaSample1() {
        return new Venta()
            .id(1L)
            .numeroComprobante("numeroComprobante1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static Venta getVentaSample2() {
        return new Venta()
            .id(2L)
            .numeroComprobante("numeroComprobante2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static Venta getVentaRandomSampleGenerator() {
        return new Venta()
            .id(longCount.incrementAndGet())
            .numeroComprobante(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
