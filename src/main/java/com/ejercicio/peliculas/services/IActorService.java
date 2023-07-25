package com.ejercicio.peliculas.services;

import java.util.List;

import com.ejercicio.peliculas.entities.Actor;

public interface IActorService {
    
    public List<Actor> findAll();
    public List<Actor> findAllById(List<Long> ids);


}
