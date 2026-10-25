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
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

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

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "celular")
    private String celular;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "calificacion_promedio")
    private Float calificacionPromedio;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "calificacionesClientes", "detalleses", "cliente" }, allowSetters = true)
    private Set<Venta> comprases = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clienteEvaluado")
    @JsonIgnoreProperties(value = { "clienteEvaluado", "evaluadorTienda", "venta" }, allowSetters = true)
    private Set<CalificacionUsuario> calificacionesRecibidases = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evaluadorTienda")
    @JsonIgnoreProperties(value = { "clienteEvaluado", "evaluadorTienda", "venta" }, allowSetters = true)
    private Set<CalificacionUsuario> calificacionesEmitidases = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "usuarioses" }, allowSetters = true)
    private Rol rol;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Usuario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Usuario apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return this.email;
    }

    public Usuario email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return this.contrasena;
    }

    public Usuario contrasena(String contrasena) {
        this.setContrasena(contrasena);
        return this;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Usuario direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return this.celular;
    }

    public Usuario celular(String celular) {
        this.setCelular(celular);
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Usuario fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Float getCalificacionPromedio() {
        return this.calificacionPromedio;
    }

    public Usuario calificacionPromedio(Float calificacionPromedio) {
        this.setCalificacionPromedio(calificacionPromedio);
        return this;
    }

    public void setCalificacionPromedio(Float calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Usuario activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Usuario usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Usuario fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Usuario usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Usuario fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<Venta> getComprases() {
        return this.comprases;
    }

    public void setComprases(Set<Venta> ventas) {
        if (this.comprases != null) {
            this.comprases.forEach(i -> i.setCliente(null));
        }
        if (ventas != null) {
            ventas.forEach(i -> i.setCliente(this));
        }
        this.comprases = ventas;
    }

    public Usuario comprases(Set<Venta> ventas) {
        this.setComprases(ventas);
        return this;
    }

    public Usuario addCompras(Venta venta) {
        this.comprases.add(venta);
        venta.setCliente(this);
        return this;
    }

    public Usuario removeCompras(Venta venta) {
        this.comprases.remove(venta);
        venta.setCliente(null);
        return this;
    }

    public Set<CalificacionUsuario> getCalificacionesRecibidases() {
        return this.calificacionesRecibidases;
    }

    public void setCalificacionesRecibidases(Set<CalificacionUsuario> calificacionUsuarios) {
        if (this.calificacionesRecibidases != null) {
            this.calificacionesRecibidases.forEach(i -> i.setClienteEvaluado(null));
        }
        if (calificacionUsuarios != null) {
            calificacionUsuarios.forEach(i -> i.setClienteEvaluado(this));
        }
        this.calificacionesRecibidases = calificacionUsuarios;
    }

    public Usuario calificacionesRecibidases(Set<CalificacionUsuario> calificacionUsuarios) {
        this.setCalificacionesRecibidases(calificacionUsuarios);
        return this;
    }

    public Usuario addCalificacionesRecibidas(CalificacionUsuario calificacionUsuario) {
        this.calificacionesRecibidases.add(calificacionUsuario);
        calificacionUsuario.setClienteEvaluado(this);
        return this;
    }

    public Usuario removeCalificacionesRecibidas(CalificacionUsuario calificacionUsuario) {
        this.calificacionesRecibidases.remove(calificacionUsuario);
        calificacionUsuario.setClienteEvaluado(null);
        return this;
    }

    public Set<CalificacionUsuario> getCalificacionesEmitidases() {
        return this.calificacionesEmitidases;
    }

    public void setCalificacionesEmitidases(Set<CalificacionUsuario> calificacionUsuarios) {
        if (this.calificacionesEmitidases != null) {
            this.calificacionesEmitidases.forEach(i -> i.setEvaluadorTienda(null));
        }
        if (calificacionUsuarios != null) {
            calificacionUsuarios.forEach(i -> i.setEvaluadorTienda(this));
        }
        this.calificacionesEmitidases = calificacionUsuarios;
    }

    public Usuario calificacionesEmitidases(Set<CalificacionUsuario> calificacionUsuarios) {
        this.setCalificacionesEmitidases(calificacionUsuarios);
        return this;
    }

    public Usuario addCalificacionesEmitidas(CalificacionUsuario calificacionUsuario) {
        this.calificacionesEmitidases.add(calificacionUsuario);
        calificacionUsuario.setEvaluadorTienda(this);
        return this;
    }

    public Usuario removeCalificacionesEmitidas(CalificacionUsuario calificacionUsuario) {
        this.calificacionesEmitidases.remove(calificacionUsuario);
        calificacionUsuario.setEvaluadorTienda(null);
        return this;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario rol(Rol rol) {
        this.setRol(rol);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return getId() != null && getId().equals(((Usuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", email='" + getEmail() + "'" +
            ", contrasena='" + getContrasena() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", celular='" + getCelular() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", calificacionPromedio=" + getCalificacionPromedio() +
            ", activo='" + getActivo() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            "}";
    }
}
