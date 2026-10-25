package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.DetalleVenta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleVentaDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer cantidadVenta;

    @NotNull
    @DecimalMin(value = "0")
    private Float precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    private Float descuento;

    @NotNull
    @DecimalMin(value = "0")
    private Float subtotal;

    private String usuarioCreacion;

    private Instant fechaCreacion;

    private String usuarioModificacion;

    private Instant fechaModificacion;

    private VentaDTO venta;

    private LibroDTO libro;

    private PromocionDTO promocionAplicada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadVenta() {
        return cantidadVenta;
    }

    public void setCantidadVenta(Integer cantidadVenta) {
        this.cantidadVenta = cantidadVenta;
    }

    public Float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
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

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    public LibroDTO getLibro() {
        return libro;
    }

    public void setLibro(LibroDTO libro) {
        this.libro = libro;
    }

    public PromocionDTO getPromocionAplicada() {
        return promocionAplicada;
    }

    public void setPromocionAplicada(PromocionDTO promocionAplicada) {
        this.promocionAplicada = promocionAplicada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleVentaDTO)) {
            return false;
        }

        DetalleVentaDTO detalleVentaDTO = (DetalleVentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, detalleVentaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVentaDTO{" +
            "id=" + getId() +
            ", cantidadVenta=" + getCantidadVenta() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", descuento=" + getDescuento() +
            ", subtotal=" + getSubtotal() +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", venta=" + getVenta() +
            ", libro=" + getLibro() +
            ", promocionAplicada=" + getPromocionAplicada() +
            "}";
    }
}
