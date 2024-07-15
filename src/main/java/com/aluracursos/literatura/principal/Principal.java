package com.aluracursos.literatura.principal;

import com.aluracursos.literatura.model.*;
import com.aluracursos.literatura.repository.AutorRepository;
import com.aluracursos.literatura.repository.LibrosRepository;
import com.aluracursos.literatura.service.ConsumoAPI;
import com.aluracursos.literatura.service.ConvierteDatos;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibrosRepository repositorio;
    private AutorRepository autorRepositorio;
    private List<Libros> libros;
    private Optional<Libros> libroBuscado;
    private List<Libros> librosBuscados = new ArrayList<>();


    public Principal(LibrosRepository repository, AutorRepository repositoryAutor) {
        this.repositorio = repository;
        this.autorRepositorio = repositoryAutor;
    }

    public void muestraElMenu() {
        var json = consumoAPI.obtenerDatos(URL_BASE);

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    BuscarYGuardarLibro();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    AutoresVivos();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");

            }
        }
    }

    private void BuscarYGuardarLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        if (datos != null && !datos.resultados().isEmpty()) {
            Optional<DatosLibros> libroBuscado = datos.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst();

            if (libroBuscado.isPresent()) {
                Libros libros = new Libros(libroBuscado.get());
                  repositorio.save(libros);
                  librosBuscados.add(libros); // Añadir el libro al ArrayList

                System.out.println("Libro guardado exitosamente: " + libroBuscado.get());
            } else {
                System.out.println("No se ha podido almacenar los datos del libro");
            }
        } else {
            System.out.println("No se encontraron resultados.");
        }
    }


    @Transactional(readOnly = true)
    private void mostrarLibrosBuscados() {
        List<Libros> libros = repositorio.findAllWithAutoresAndIdiomas();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros encontrados en la base de datos:");
            for (Libros libro : libros) {
                System.out.println(libro);
            }
        }
    }


    @Transactional(readOnly = true)
    private void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAllAutores();
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores en la base de datos.");
        } else {
            System.out.println("Autores encontrados en la base de datos:");
            for (Autor autor : autores) {
                System.out.println(autor);
            }
        }
    }


    private void AutoresVivos() {
        System.out.println("Ingrese el año para listar autores vivos:");
        String year = teclado.nextLine();
        List<Autor> autoresVivos = autorRepositorio.findAutoresVivos(year);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + year);
        } else {
            System.out.println("Autores vivos en el año " + year + ":");
            for (Autor autor : autoresVivos) {
                System.out.println(autor);
            }
        }
    }

    private void mostrarLibrosPorIdioma() {
        List<Libros> libros = repositorio.findAllOrderByIdioma();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros agrupados por idioma:");
            String idiomaActual = "";
            for (Libros libro : libros) {
                if (!libro.getIdiomas().equals(idiomaActual)) {
                    idiomaActual = libro.getIdiomas().toString();
                    System.out.println("Idioma: " + idiomaActual);
                }
                System.out.println("  " + libro);
            }
        }
    }


}
