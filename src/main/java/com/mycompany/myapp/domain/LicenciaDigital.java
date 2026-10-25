package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.EstadoLicencia;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A LicenciaDigital.
 */
@Entity
@Table(name = "licencia_digital")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LicenciaDigital implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "codigo_licencia", nullable = false, unique = true)
    private String codigoLicencia;

    @Column(name = "enlace_descarga")
    private String enlaceDescarga;

    @Column(name = "fecha_asignacion")
    private Instant fechaAsignacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoLicencia estado;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "detallesVentas", "licenciases", "categoriases", "autoreses", "editorial", "promocioneses" },
        allowSetters = true
    )
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "licenciasEntregadases", "venta", "libro", "promocionAplicada" }, allowSetters = true)
    private DetalleVenta detalleVenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LicenciaDigital id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoLicencia() {
        return this.codigoLicencia;
    }

    public LicenciaDigital codigoLicencia(String codigoLicencia) {
        this.setCodigoLicencia(codigoLicencia);
        return this;
    }

    public void setCodigoLicencia(String codigoLicencia) {
        this.codigoLicencia = codigoLicencia;
    }

    public String getEnlaceDescarga() {
        return this.enlaceDescarga;
    }

    public LicenciaDigital enlaceDescarga(String enlaceDescarga) {
        this.setEnlaceDescarga(enlaceDescarga);
        return this;
    }

    public void setEnlaceDescarga(String enlaceDescarga) {
        this.enlaceDescarga = enlaceDescarga;
    }

    public Instant getFechaAsignacion() {
        return this.fechaAsignacion;
    }

    public LicenciaDigital fechaAsignacion(Instant fechaAsignacion) {
        this.setFechaAsignacion(fechaAsignacion);
        return this;
    }

    public void setFechaAsignacion(Instant fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public EstadoLicencia getEstado() {
        return this.estado;
    }

    public LicenciaDigital estado(EstadoLicencia estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoLicencia estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public LicenciaDigital usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public LicenciaDigital fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public LicenciaDigital usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public LicenciaDigital fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LicenciaDigital libro(Libro libro) {
        this.setLibro(libro);
        return this;
    }

    public DetalleVenta getDetalleVenta() {
        return this.detalleVenta;
    }

    public void setDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public LicenciaDigital detalleVenta(DetalleVenta detalleVenta) {
        this.setDetalleVenta(detalleVenta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LicenciaDigital)) {
            return false;
        }
        return getId() != null && getId().equals(((LicenciaDigital) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LicenciaDigital{" +
            "id=" + getId() +
            ", codigoLicencia='" + getCodigoLicencia() + "'" +
            ", enlaceDescarga='" + getEnlaceDescarga() + "'" +
            ", fechaAsignacion='" + getFechaAsignacion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
