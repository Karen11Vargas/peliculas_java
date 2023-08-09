package com.ejercicio.peliculas.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ejercicio.peliculas.entities.Peliculas;

public interface IPeliculaRepository extends JpaRepository<Peliculas, Long> {

}
