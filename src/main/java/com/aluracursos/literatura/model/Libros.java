package com.aluracursos.literatura.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ElementCollection
    private Set<String> idiomas;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores;


    public Libros() {
    }


    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autores = datosLibros.autor().stream().map(da -> {
            Autor autor = new Autor();
            autor.setNombre(da.nombre());
            autor.setFechaDeNacimiento(da.fechaDeNacimiento());
            return autor;
        }).collect(Collectors.toSet());
        this.idiomas = new HashSet<>(datosLibros.idiomas());
    }

//    @Override
//    public String toString() {
//        return "titulo='" + titulo +
//                "autor=" + autores +
//                "lenguaje=" + idiomas;
//    }

    @Override
    public String toString() {
        String autoresStr = autores != null ? autores.toString() : "No inicializado";
        String idiomasStr = idiomas != null ? idiomas.toString() : "No inicializado";

        return "titulo='" + titulo + "', " +
                "autor=" + autoresStr + ", " +
                "lenguaje=" + idiomasStr;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getTitulo() {

        return titulo;
    }

    public void setTitulo(String titulo) {

        this.titulo = titulo;
    }

    public Set<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Set<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

}
