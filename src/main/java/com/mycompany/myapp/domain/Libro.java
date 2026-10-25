package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Libro.
 */
@Entity
@Table(name = "libro")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Libro implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Lob
    @Column(name = "sinopsis")
    private String sinopsis;

    @Column(name = "url_portada")
    private String urlPortada;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio", nullable = false)
    private Float precio;

    @NotNull
    @Column(name = "idioma", nullable = false)
    private String idioma;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "libro")
    @JsonIgnoreProperties(value = { "licenciasEntregadases", "venta", "libro", "promocionAplicada" }, allowSetters = true)
    private Set<DetalleVenta> detallesVentas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "libro")
    @JsonIgnoreProperties(value = { "libro", "detalleVenta" }, allowSetters = true)
    private Set<LicenciaDigital> licenciases = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_libro__categorias",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "categorias_id")
    )
    @JsonIgnoreProperties(value = { "libroses" }, allowSetters = true)
    private Set<Categoria> categoriases = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_libro__autores",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "autores_id")
    )
    @JsonIgnoreProperties(value = { "libroses" }, allowSetters = true)
    private Set<Autor> autoreses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "libroses" }, allowSetters = true)
    private Editorial editorial;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "libroses")
    @JsonIgnoreProperties(value = { "detallesVentaAplicadoses", "libroses" }, allowSetters = true)
    private Set<Promocion> promocioneses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Libro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public Libro isbn(String isbn) {
        this.setIsbn(isbn);
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Libro titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return this.sinopsis;
    }

    public Libro sinopsis(String sinopsis) {
        this.setSinopsis(sinopsis);
        return this;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getUrlPortada() {
        return this.urlPortada;
    }

    public Libro urlPortada(String urlPortada) {
        this.setUrlPortada(urlPortada);
        return this;
    }

    public void setUrlPortada(String urlPortada) {
        this.urlPortada = urlPortada;
    }

    public LocalDate getFechaPublicacion() {
        return this.fechaPublicacion;
    }

    public Libro fechaPublicacion(LocalDate fechaPublicacion) {
        this.setFechaPublicacion(fechaPublicacion);
        return this;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Libro precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getIdioma() {
        return this.idioma;
    }

    public Libro idioma(String idioma) {
        this.setIdioma(idioma);
        return this;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Libro activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Libro usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Libro fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Libro usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Libro fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<DetalleVenta> getDetallesVentas() {
        return this.detallesVentas;
    }

    public void setDetallesVentas(Set<DetalleVenta> detalleVentas) {
        if (this.detallesVentas != null) {
            this.detallesVentas.forEach(i -> i.setLibro(null));
        }
        if (detalleVentas != null) {
            detalleVentas.forEach(i -> i.setLibro(this));
        }
        this.detallesVentas = detalleVentas;
    }

    public Libro detallesVentas(Set<DetalleVenta> detalleVentas) {
        this.setDetallesVentas(detalleVentas);
        return this;
    }

    public Libro addDetallesVenta(DetalleVenta detalleVenta) {
        this.detallesVentas.add(detalleVenta);
        detalleVenta.setLibro(this);
        return this;
    }

    public Libro removeDetallesVenta(DetalleVenta detalleVenta) {
        this.detallesVentas.remove(detalleVenta);
        detalleVenta.setLibro(null);
        return this;
    }

    public Set<LicenciaDigital> getLicenciases() {
        return this.licenciases;
    }

    public void setLicenciases(Set<LicenciaDigital> licenciaDigitals) {
        if (this.licenciases != null) {
            this.licenciases.forEach(i -> i.setLibro(null));
        }
        if (licenciaDigitals != null) {
            licenciaDigitals.forEach(i -> i.setLibro(this));
        }
        this.licenciases = licenciaDigitals;
    }

    public Libro licenciases(Set<LicenciaDigital> licenciaDigitals) {
        this.setLicenciases(licenciaDigitals);
        return this;
    }

    public Libro addLicencias(LicenciaDigital licenciaDigital) {
        this.licenciases.add(licenciaDigital);
        licenciaDigital.setLibro(this);
        return this;
    }

    public Libro removeLicencias(LicenciaDigital licenciaDigital) {
        this.licenciases.remove(licenciaDigital);
        licenciaDigital.setLibro(null);
        return this;
    }

    public Set<Categoria> getCategoriases() {
        return this.categoriases;
    }

    public void setCategoriases(Set<Categoria> categorias) {
        this.categoriases = categorias;
    }

    public Libro categoriases(Set<Categoria> categorias) {
        this.setCategoriases(categorias);
        return this;
    }

    public Libro addCategorias(Categoria categoria) {
        this.categoriases.add(categoria);
        return this;
    }

    public Libro removeCategorias(Categoria categoria) {
        this.categoriases.remove(categoria);
        return this;
    }

    public Set<Autor> getAutoreses() {
        return this.autoreses;
    }

    public void setAutoreses(Set<Autor> autors) {
        this.autoreses = autors;
    }

    public Libro autoreses(Set<Autor> autors) {
        this.setAutoreses(autors);
        return this;
    }

    public Libro addAutores(Autor autor) {
        this.autoreses.add(autor);
        return this;
    }

    public Libro removeAutores(Autor autor) {
        this.autoreses.remove(autor);
        return this;
    }

    public Editorial getEditorial() {
        return this.editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Libro editorial(Editorial editorial) {
        this.setEditorial(editorial);
        return this;
    }

    public Set<Promocion> getPromocioneses() {
        return this.promocioneses;
    }

    public void setPromocioneses(Set<Promocion> promocions) {
        if (this.promocioneses != null) {
            this.promocioneses.forEach(i -> i.removeLibros(this));
        }
        if (promocions != null) {
            promocions.forEach(i -> i.addLibros(this));
        }
        this.promocioneses = promocions;
    }

    public Libro promocioneses(Set<Promocion> promocions) {
        this.setPromocioneses(promocions);
        return this;
    }

    public Libro addPromociones(Promocion promocion) {
        this.promocioneses.add(promocion);
        promocion.getLibroses().add(this);
        return this;
    }

    public Libro removePromociones(Promocion promocion) {
        this.promocioneses.remove(promocion);
        promocion.getLibroses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Libro)) {
            return false;
        }
        return getId() != null && getId().equals(((Libro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Libro{" +
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
            "}";
    }
}
