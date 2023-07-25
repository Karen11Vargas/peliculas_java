package com.ejercicio.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.ejercicio.peliculas.entities.Peliculas;

public interface IPeliculaRepository extends CrudRepository<Peliculas, Long> {

}
