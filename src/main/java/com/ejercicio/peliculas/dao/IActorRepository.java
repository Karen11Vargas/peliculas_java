package com.ejercicio.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.ejercicio.peliculas.entities.Actor;

public interface IActorRepository extends CrudRepository<Actor, Long>{
    
}
