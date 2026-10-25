package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Libro} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LibroDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String isbn;

    @NotNull
    @Size(max = 255)
    private String titulo;

    @Lob
    private String sinopsis;

    private String urlPortada;

    private LocalDate fechaPublicacion;

    @NotNull
    @DecimalMin(value = "0")
    private Float precio;

    @NotNull
    private String idioma;

    @NotNull
    private Boolean activo;

    private String usuarioCreacion;

    private Instant fechaCreacion;

    private String usuarioModificacion;

    private Instant fechaModificacion;

    private Set<CategoriaDTO> categoriases = new HashSet<>();

    private Set<AutorDTO> autoreses = new HashSet<>();

    private EditorialDTO editorial;

    private Set<PromocionDTO> promocioneses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getUrlPortada() {
        return urlPortada;
    }

    public void setUrlPortada(String urlPortada) {
        this.urlPortada = urlPortada;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
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

    public Set<CategoriaDTO> getCategoriases() {
        return categoriases;
    }

    public void setCategoriases(Set<CategoriaDTO> categoriases) {
        this.categoriases = categoriases;
    }

    public Set<AutorDTO> getAutoreses() {
        return autoreses;
    }

    public void setAutoreses(Set<AutorDTO> autoreses) {
        this.autoreses = autoreses;
    }

    public EditorialDTO getEditorial() {
        return editorial;
    }

    public void setEditorial(EditorialDTO editorial) {
        this.editorial = editorial;
    }

    public Set<PromocionDTO> getPromocioneses() {
        return promocioneses;
    }

    public void setPromocioneses(Set<PromocionDTO> promocioneses) {
        this.promocioneses = promocioneses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibroDTO)) {
            return false;
        }

        LibroDTO libroDTO = (LibroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, libroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibroDTO{" +
            "id=" + getId() +
            ", isbn='" + getIsbn() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", sinopsis='" + getSinopsis() + "'" +
            ", urlPortada='" + getUrlPortada() + "'" +
            ", fechaPublicacion='" + getFechaPublicacion() + "'" +
            ", precio=" + getPrecio() +
            ", idioma='" + getIdioma() + "'" +
            ", activo='" + getActivo() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", categoriases=" + getCategoriases() +
            ", autoreses=" + getAutoreses() +
            ", editorial=" + getEditorial() +
            ", promocioneses=" + getPromocioneses() +
            "}";
    }
}
