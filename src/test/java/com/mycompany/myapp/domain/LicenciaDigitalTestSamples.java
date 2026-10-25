package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LicenciaDigitalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static LicenciaDigital getLicenciaDigitalSample1() {
        return new LicenciaDigital()
            .id(1L)
            .codigoLicencia("codigoLicencia1")
            .enlaceDescarga("enlaceDescarga1")
            .usuarioCreacion("usuarioCreacion1")
            .usuarioModificacion("usuarioModificacion1");
    }

    public static LicenciaDigital getLicenciaDigitalSample2() {
        return new LicenciaDigital()
            .id(2L)
            .codigoLicencia("codigoLicencia2")
            .enlaceDescarga("enlaceDescarga2")
            .usuarioCreacion("usuarioCreacion2")
            .usuarioModificacion("usuarioModificacion2");
    }

    public static LicenciaDigital getLicenciaDigitalRandomSampleGenerator() {
        return new LicenciaDigital()
            .id(longCount.incrementAndGet())
            .codigoLicencia(UUID.randomUUID().toString())
            .enlaceDescarga(UUID.randomUUID().toString())
            .usuarioCreacion(UUID.randomUUID().toString())
            .usuarioModificacion(UUID.randomUUID().toString());
    }
}
