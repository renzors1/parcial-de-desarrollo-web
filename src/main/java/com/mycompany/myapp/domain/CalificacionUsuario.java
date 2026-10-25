package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A CalificacionUsuario.
 */
@Entity
@Table(name = "calificacion_usuario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CalificacionUsuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @Size(max = 500)
    @Column(name = "comentario", length = 500)
    private String comentario;

    @NotNull
    @Column(name = "fecha_calificacion", nullable = false)
    private Instant fechaCalificacion;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "comprases", "calificacionesRecibidases", "calificacionesEmitidases", "rol" }, allowSetters = true)
    private Usuario clienteEvaluado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comprases", "calificacionesRecibidases", "calificacionesEmitidases", "rol" }, allowSetters = true)
    private Usuario evaluadorTienda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "calificacionesClientes", "detalleses", "cliente" }, allowSetters = true)
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CalificacionUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuntuacion() {
        return this.puntuacion;
    }

    public CalificacionUsuario puntuacion(Integer puntuacion) {
        this.setPuntuacion(puntuacion);
        return this;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return this.comentario;
    }

    public CalificacionUsuario comentario(String comentario) {
        this.setComentario(comentario);
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Instant getFechaCalificacion() {
        return this.fechaCalificacion;
    }

    public CalificacionUsuario fechaCalificacion(Instant fechaCalificacion) {
        this.setFechaCalificacion(fechaCalificacion);
        return this;
    }

    public void setFechaCalificacion(Instant fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public CalificacionUsuario activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public CalificacionUsuario usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public CalificacionUsuario fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public CalificacionUsuario usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public CalificacionUsuario fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getClienteEvaluado() {
        return this.clienteEvaluado;
    }

    public void setClienteEvaluado(Usuario usuario) {
        this.clienteEvaluado = usuario;
    }

    public CalificacionUsuario clienteEvaluado(Usuario usuario) {
        this.setClienteEvaluado(usuario);
        return this;
    }

    public Usuario getEvaluadorTienda() {
        return this.evaluadorTienda;
    }

    public void setEvaluadorTienda(Usuario usuario) {
        this.evaluadorTienda = usuario;
    }

    public CalificacionUsuario evaluadorTienda(Usuario usuario) {
        this.setEvaluadorTienda(usuario);
        return this;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public CalificacionUsuario venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalificacionUsuario)) {
            return false;
        }
        return getId() != null && getId().equals(((CalificacionUsuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalificacionUsuario{" +
            "id=" + getId() +
            ", puntuacion=" + getPuntuacion() +
            ", comentario='" + getComentario() + "'" +
            ", fechaCalificacion='" + getFechaCalificacion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
