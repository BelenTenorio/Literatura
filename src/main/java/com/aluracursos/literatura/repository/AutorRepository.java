package com.aluracursos.literatura.repository;

import com.aluracursos.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AutorRepository extends JpaRepository<Autor,Long>{

    @Query("SELECT a FROM Autor a")
    List<Autor> findAllAutores();

    @Query("SELECT a FROM Autor a WHERE a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento > :year")
    List<Autor> findAutoresVivos(String year);
}

