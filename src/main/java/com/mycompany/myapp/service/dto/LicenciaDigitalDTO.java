package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.EstadoLicencia;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.LicenciaDigital} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LicenciaDigitalDTO implements Serializable {

    private Long id;

    @NotNull
    private String codigoLicencia;

    private String enlaceDescarga;

    private Instant fechaAsignacion;

    @NotNull
    private EstadoLicencia estado;

    private String usuarioCreacion;

    private Instant fechaCreacion;

    private String usuarioModificacion;

    private Instant fechaModificacion;

    private LibroDTO libro;

    private DetalleVentaDTO detalleVenta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoLicencia() {
        return codigoLicencia;
    }

    public void setCodigoLicencia(String codigoLicencia) {
        this.codigoLicencia = codigoLicencia;
    }

    public String getEnlaceDescarga() {
        return enlaceDescarga;
    }

    public void setEnlaceDescarga(String enlaceDescarga) {
        this.enlaceDescarga = enlaceDescarga;
    }

    public Instant getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Instant fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public EstadoLicencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoLicencia estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LibroDTO getLibro() {
        return libro;
    }

    public void setLibro(LibroDTO libro) {
        this.libro = libro;
    }

    public DetalleVentaDTO getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVentaDTO detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LicenciaDigitalDTO)) {
            return false;
        }

        LicenciaDigitalDTO licenciaDigitalDTO = (LicenciaDigitalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, licenciaDigitalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LicenciaDigitalDTO{" +
            "id=" + getId() +
            ", codigoLicencia='" + getCodigoLicencia() + "'" +
            ", enlaceDescarga='" + getEnlaceDescarga() + "'" +
            ", fechaAsignacion='" + getFechaAsignacion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", libro=" + getLibro() +
            ", detalleVenta=" + getDetalleVenta() +
            "}";
    }
}
