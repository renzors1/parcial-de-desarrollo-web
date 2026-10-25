package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DetalleVenta.
 */
@Entity
@Table(name = "detalle_venta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetalleVenta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad_venta", nullable = false)
    private Integer cantidadVenta;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_unitario", nullable = false)
    private Float precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "descuento", nullable = false)
    private Float descuento;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", nullable = false)
    private Float subtotal;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "detalleVenta")
    @JsonIgnoreProperties(value = { "libro", "detalleVenta" }, allowSetters = true)
    private Set<LicenciaDigital> licenciasEntregadases = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "calificacionesClientes", "detalleses", "cliente" }, allowSetters = true)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "detallesVentas", "licenciases", "categoriases", "autoreses", "editorial", "promocioneses" },
        allowSetters = true
    )
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "detallesVentaAplicadoses", "libroses" }, allowSetters = true)
    private Promocion promocionAplicada;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadVenta() {
        return this.cantidadVenta;
    }

    public DetalleVenta cantidadVenta(Integer cantidadVenta) {
        this.setCantidadVenta(cantidadVenta);
        return this;
    }

    public void setCantidadVenta(Integer cantidadVenta) {
        this.cantidadVenta = cantidadVenta;
    }

    public Float getPrecioUnitario() {
        return this.precioUnitario;
    }

    public DetalleVenta precioUnitario(Float precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Float getDescuento() {
        return this.descuento;
    }

    public DetalleVenta descuento(Float descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Float getSubtotal() {
        return this.subtotal;
    }

    public DetalleVenta subtotal(Float subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public DetalleVenta usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public DetalleVenta fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public DetalleVenta usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public DetalleVenta fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<LicenciaDigital> getLicenciasEntregadases() {
        return this.licenciasEntregadases;
    }

    public void setLicenciasEntregadases(Set<LicenciaDigital> licenciaDigitals) {
        if (this.licenciasEntregadases != null) {
            this.licenciasEntregadases.forEach(i -> i.setDetalleVenta(null));
        }
        if (licenciaDigitals != null) {
            licenciaDigitals.forEach(i -> i.setDetalleVenta(this));
        }
        this.licenciasEntregadases = licenciaDigitals;
    }

    public DetalleVenta licenciasEntregadases(Set<LicenciaDigital> licenciaDigitals) {
        this.setLicenciasEntregadases(licenciaDigitals);
        return this;
    }

    public DetalleVenta addLicenciasEntregadas(LicenciaDigital licenciaDigital) {
        this.licenciasEntregadases.add(licenciaDigital);
        licenciaDigital.setDetalleVenta(this);
        return this;
    }

    public DetalleVenta removeLicenciasEntregadas(LicenciaDigital licenciaDigital) {
        this.licenciasEntregadases.remove(licenciaDigital);
        licenciaDigital.setDetalleVenta(null);
        return this;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DetalleVenta venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public DetalleVenta libro(Libro libro) {
        this.setLibro(libro);
        return this;
    }

    public Promocion getPromocionAplicada() {
        return this.promocionAplicada;
    }

    public void setPromocionAplicada(Promocion promocion) {
        this.promocionAplicada = promocion;
    }

    public DetalleVenta promocionAplicada(Promocion promocion) {
        this.setPromocionAplicada(promocion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleVenta)) {
            return false;
        }
        return getId() != null && getId().equals(((DetalleVenta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVenta{" +
            "id=" + getId() +
            ", cantidadVenta=" + getCantidadVenta() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", descuento=" + getDescuento() +
            ", subtotal=" + getSubtotal() +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
