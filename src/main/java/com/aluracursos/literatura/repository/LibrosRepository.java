package com.aluracursos.literatura.repository;

import com.aluracursos.literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;


public interface LibrosRepository extends JpaRepository<Libros,Long> {
    Optional<Libros> findByTituloContainsIgnoreCase(String nombreLibro);

    @Query("SELECT l FROM Libros l JOIN FETCH l.autores a JOIN FETCH l.idiomas")
    List<Libros> findAllWithAutoresAndIdiomas();

    @Query("SELECT l FROM Libros l ORDER BY l.idiomas")
    List<Libros> findAllOrderByIdioma();

}
