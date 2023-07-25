package com.ejercicio.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.ejercicio.peliculas.entities.Genero;

public interface IGeneroRepository extends CrudRepository<Genero, Long>{
}
