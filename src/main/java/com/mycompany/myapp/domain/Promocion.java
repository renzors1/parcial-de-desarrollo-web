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
 * A Promocion.
 */
@Entity
@Table(name = "promocion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Promocion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "porcentaje_descuento", nullable = false)
    private Float porcentajeDescuento;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad_definida", nullable = false)
    private Integer cantidadDefinida;

    @NotNull
    @Min(value = 0)
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private Instant fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private Instant fechaFin;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "promocionAplicada")
    @JsonIgnoreProperties(value = { "licenciasEntregadases", "venta", "libro", "promocionAplicada" }, allowSetters = true)
    private Set<DetalleVenta> detallesVentaAplicadoses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_promocion__libros",
        joinColumns = @JoinColumn(name = "promocion_id"),
        inverseJoinColumns = @JoinColumn(name = "libros_id")
    )
    @JsonIgnoreProperties(
        value = { "detallesVentas", "licenciases", "categoriases", "autoreses", "editorial", "promocioneses" },
        allowSetters = true
    )
    private Set<Libro> libroses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Promocion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Promocion nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Promocion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPorcentajeDescuento() {
        return this.porcentajeDescuento;
    }

    public Promocion porcentajeDescuento(Float porcentajeDescuento) {
        this.setPorcentajeDescuento(porcentajeDescuento);
        return this;
    }

    public void setPorcentajeDescuento(Float porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Integer getCantidadDefinida() {
        return this.cantidadDefinida;
    }

    public Promocion cantidadDefinida(Integer cantidadDefinida) {
        this.setCantidadDefinida(cantidadDefinida);
        return this;
    }

    public void setCantidadDefinida(Integer cantidadDefinida) {
        this.cantidadDefinida = cantidadDefinida;
    }

    public Integer getCantidadDisponible() {
        return this.cantidadDisponible;
    }

    public Promocion cantidadDisponible(Integer cantidadDisponible) {
        this.setCantidadDisponible(cantidadDisponible);
        return this;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Promocion fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Promocion fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Promocion activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Promocion usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Promocion fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Promocion usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Promocion fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<DetalleVenta> getDetallesVentaAplicadoses() {
        return this.detallesVentaAplicadoses;
    }

    public void setDetallesVentaAplicadoses(Set<DetalleVenta> detalleVentas) {
        if (this.detallesVentaAplicadoses != null) {
            this.detallesVentaAplicadoses.forEach(i -> i.setPromocionAplicada(null));
        }
        if (detalleVentas != null) {
            detalleVentas.forEach(i -> i.setPromocionAplicada(this));
        }
        this.detallesVentaAplicadoses = detalleVentas;
    }

    public Promocion detallesVentaAplicadoses(Set<DetalleVenta> detalleVentas) {
        this.setDetallesVentaAplicadoses(detalleVentas);
        return this;
    }

    public Promocion addDetallesVentaAplicados(DetalleVenta detalleVenta) {
        this.detallesVentaAplicadoses.add(detalleVenta);
        detalleVenta.setPromocionAplicada(this);
        return this;
    }

    public Promocion removeDetallesVentaAplicados(DetalleVenta detalleVenta) {
        this.detallesVentaAplicadoses.remove(detalleVenta);
        detalleVenta.setPromocionAplicada(null);
        return this;
    }

    public Set<Libro> getLibroses() {
        return this.libroses;
    }

    public void setLibroses(Set<Libro> libros) {
        this.libroses = libros;
    }

    public Promocion libroses(Set<Libro> libros) {
        this.setLibroses(libros);
        return this;
    }

    public Promocion addLibros(Libro libro) {
        this.libroses.add(libro);
        return this;
    }

    public Promocion removeLibros(Libro libro) {
        this.libroses.remove(libro);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promocion)) {
            return false;
        }
        return getId() != null && getId().equals(((Promocion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promocion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", porcentajeDescuento=" + getPorcentajeDescuento() +
            ", cantidadDefinida=" + getCantidadDefinida() +
            ", cantidadDisponible=" + getCantidadDisponible() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", activo='" + getActivo() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
