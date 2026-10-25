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
 * A Venta.
 */
@Entity
@Table(name = "venta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_comprobante", nullable = false, unique = true)
    private String numeroComprobante;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private Instant fechaVenta;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal_venta", nullable = false)
    private Float subtotalVenta;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_descuento", nullable = false)
    private Float totalDescuento;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "igv", nullable = false)
    private Float igv;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_venta", nullable = false)
    private Float totalVenta;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta")
    @JsonIgnoreProperties(value = { "clienteEvaluado", "evaluadorTienda", "venta" }, allowSetters = true)
    private Set<CalificacionUsuario> calificacionesClientes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta")
    @JsonIgnoreProperties(value = { "licenciasEntregadases", "venta", "libro", "promocionAplicada" }, allowSetters = true)
    private Set<DetalleVenta> detalleses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comprases", "calificacionesRecibidases", "calificacionesEmitidases", "rol" }, allowSetters = true)
    private Usuario cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroComprobante() {
        return this.numeroComprobante;
    }

    public Venta numeroComprobante(String numeroComprobante) {
        this.setNumeroComprobante(numeroComprobante);
        return this;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Instant getFechaVenta() {
        return this.fechaVenta;
    }

    public Venta fechaVenta(Instant fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Float getSubtotalVenta() {
        return this.subtotalVenta;
    }

    public Venta subtotalVenta(Float subtotalVenta) {
        this.setSubtotalVenta(subtotalVenta);
        return this;
    }

    public void setSubtotalVenta(Float subtotalVenta) {
        this.subtotalVenta = subtotalVenta;
    }

    public Float getTotalDescuento() {
        return this.totalDescuento;
    }

    public Venta totalDescuento(Float totalDescuento) {
        this.setTotalDescuento(totalDescuento);
        return this;
    }

    public void setTotalDescuento(Float totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public Float getIgv() {
        return this.igv;
    }

    public Venta igv(Float igv) {
        this.setIgv(igv);
        return this;
    }

    public void setIgv(Float igv) {
        this.igv = igv;
    }

    public Float getTotalVenta() {
        return this.totalVenta;
    }

    public Venta totalVenta(Float totalVenta) {
        this.setTotalVenta(totalVenta);
        return this;
    }

    public void setTotalVenta(Float totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Venta usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Venta fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Venta usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Venta fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<CalificacionUsuario> getCalificacionesClientes() {
        return this.calificacionesClientes;
    }

    public void setCalificacionesClientes(Set<CalificacionUsuario> calificacionUsuarios) {
        if (this.calificacionesClientes != null) {
            this.calificacionesClientes.forEach(i -> i.setVenta(null));
        }
        if (calificacionUsuarios != null) {
            calificacionUsuarios.forEach(i -> i.setVenta(this));
        }
        this.calificacionesClientes = calificacionUsuarios;
    }

    public Venta calificacionesClientes(Set<CalificacionUsuario> calificacionUsuarios) {
        this.setCalificacionesClientes(calificacionUsuarios);
        return this;
    }

    public Venta addCalificacionesCliente(CalificacionUsuario calificacionUsuario) {
        this.calificacionesClientes.add(calificacionUsuario);
        calificacionUsuario.setVenta(this);
        return this;
    }

    public Venta removeCalificacionesCliente(CalificacionUsuario calificacionUsuario) {
        this.calificacionesClientes.remove(calificacionUsuario);
        calificacionUsuario.setVenta(null);
        return this;
    }

    public Set<DetalleVenta> getDetalleses() {
        return this.detalleses;
    }

    public void setDetalleses(Set<DetalleVenta> detalleVentas) {
        if (this.detalleses != null) {
            this.detalleses.forEach(i -> i.setVenta(null));
        }
        if (detalleVentas != null) {
            detalleVentas.forEach(i -> i.setVenta(this));
        }
        this.detalleses = detalleVentas;
    }

    public Venta detalleses(Set<DetalleVenta> detalleVentas) {
        this.setDetalleses(detalleVentas);
        return this;
    }

    public Venta addDetalles(DetalleVenta detalleVenta) {
        this.detalleses.add(detalleVenta);
        detalleVenta.setVenta(this);
        return this;
    }

    public Venta removeDetalles(DetalleVenta detalleVenta) {
        this.detalleses.remove(detalleVenta);
        detalleVenta.setVenta(null);
        return this;
    }

    public Usuario getCliente() {
        return this.cliente;
    }

    public void setCliente(Usuario usuario) {
        this.cliente = usuario;
    }

    public Venta cliente(Usuario usuario) {
        this.setCliente(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return getId() != null && getId().equals(((Venta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
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
            "}";
    }
}
