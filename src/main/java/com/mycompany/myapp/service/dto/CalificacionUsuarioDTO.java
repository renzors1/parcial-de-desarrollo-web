package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CalificacionUsuario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CalificacionUsuarioDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer puntuacion;

    @Size(max = 500)
    private String comentario;

    @NotNull
    private Instant fechaCalificacion;

    @NotNull
    private Boolean activo;

    private String usuarioCreacion;

    private Instant fechaCreacion;

    private String usuarioModificacion;

    private Instant fechaModificacion;

    @NotNull
    private UsuarioDTO clienteEvaluado;

    private UsuarioDTO evaluadorTienda;

    private VentaDTO venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Instant getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(Instant fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public UsuarioDTO getClienteEvaluado() {
        return clienteEvaluado;
    }

    public void setClienteEvaluado(UsuarioDTO clienteEvaluado) {
        this.clienteEvaluado = clienteEvaluado;
    }

    public UsuarioDTO getEvaluadorTienda() {
        return evaluadorTienda;
    }

    public void setEvaluadorTienda(UsuarioDTO evaluadorTienda) {
        this.evaluadorTienda = evaluadorTienda;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalificacionUsuarioDTO)) {
            return false;
        }

        CalificacionUsuarioDTO calificacionUsuarioDTO = (CalificacionUsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, calificacionUsuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalificacionUsuarioDTO{" +
            "id=" + getId() +
            ", puntuacion=" + getPuntuacion() +
            ", comentario='" + getComentario() + "'" +
            ", fechaCalificacion='" + getFechaCalificacion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", clienteEvaluado=" + getClienteEvaluado() +
            ", evaluadorTienda=" + getEvaluadorTienda() +
            ", venta=" + getVenta() +
            "}";
    }
}
