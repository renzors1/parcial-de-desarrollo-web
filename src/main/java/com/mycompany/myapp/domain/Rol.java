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
 * A Rol.
 */
@Entity
@Table(name = "rol")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rol implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "codigo_identificador", nullable = false, unique = true)
    private Integer codigoIdentificador;

    @NotNull
    @Column(name = "nombre_rol", nullable = false, unique = true)
    private String nombreRol;

    @Column(name = "descripcion")
    private String descripcion;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
    @JsonIgnoreProperties(value = { "comprases", "calificacionesRecibidases", "calificacionesEmitidases", "rol" }, allowSetters = true)
    private Set<Usuario> usuarioses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigoIdentificador() {
        return this.codigoIdentificador;
    }

    public Rol codigoIdentificador(Integer codigoIdentificador) {
        this.setCodigoIdentificador(codigoIdentificador);
        return this;
    }

    public void setCodigoIdentificador(Integer codigoIdentificador) {
        this.codigoIdentificador = codigoIdentificador;
    }

    public String getNombreRol() {
        return this.nombreRol;
    }

    public Rol nombreRol(String nombreRol) {
        this.setNombreRol(nombreRol);
        return this;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Rol descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Rol activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Rol usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Rol fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Rol usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Rol fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<Usuario> getUsuarioses() {
        return this.usuarioses;
    }

    public void setUsuarioses(Set<Usuario> usuarios) {
        if (this.usuarioses != null) {
            this.usuarioses.forEach(i -> i.setRol(null));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.setRol(this));
        }
        this.usuarioses = usuarios;
    }

    public Rol usuarioses(Set<Usuario> usuarios) {
        this.setUsuarioses(usuarios);
        return this;
    }

    public Rol addUsuarios(Usuario usuario) {
        this.usuarioses.add(usuario);
        usuario.setRol(this);
        return this;
    }

    public Rol removeUsuarios(Usuario usuario) {
        this.usuarioses.remove(usuario);
        usuario.setRol(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rol)) {
            return false;
        }
        return getId() != null && getId().equals(((Rol) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rol{" +
            "id=" + getId() +
            ", codigoIdentificador=" + getCodigoIdentificador() +
            ", nombreRol='" + getNombreRol() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
