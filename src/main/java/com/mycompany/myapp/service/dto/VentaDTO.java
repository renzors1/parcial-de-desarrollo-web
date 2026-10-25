package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Venta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentaDTO implements Serializable {

    private Long id;

    @NotNull
    private String numeroComprobante;

    @NotNull
    private Instant fechaVenta;

    @NotNull
    @DecimalMin(value = "0")
    private Float subtotalVenta;

    @NotNull
    @DecimalMin(value = "0")
    private Float totalDescuento;

    @NotNull
    @DecimalMin(value = "0")
    private Float igv;

    @NotNull
    @DecimalMin(value = "0")
    private Float totalVenta;

    private String usuarioCreacion;

    private Instant fechaCreacion;

    private String usuarioModificacion;

    private Instant fechaModificacion;

    private UsuarioDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Instant getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Float getSubtotalVenta() {
        return subtotalVenta;
    }

    public void setSubtotalVenta(Float subtotalVenta) {
        this.subtotalVenta = subtotalVenta;
    }

    public Float getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(Float totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public Float getIgv() {
        return igv;
    }

    public void setIgv(Float igv) {
        this.igv = igv;
    }

    public Float getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(Float totalVenta) {
        this.totalVenta = totalVenta;
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

    public UsuarioDTO getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaDTO)) {
            return false;
        }

        VentaDTO ventaDTO = (VentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaDTO{" +
            "id=" + getId() +
            ", numeroComprobante='" + getNumeroComprobante() + "'" +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", subtotalVenta=" + getSubtotalVenta() +
            ", totalDescuento=" + getTotalDescuento() +
            ", igv=" + getIgv() +
            ", totalVenta=" + getTotalVenta() +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", cliente=" + getCliente() +
            "}";
    }
}
