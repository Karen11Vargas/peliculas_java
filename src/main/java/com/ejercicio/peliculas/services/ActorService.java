package com.ejercicio.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ejercicio.peliculas.dao.IActorRepository;
import com.ejercicio.peliculas.entities.Actor;

@Service
public class ActorService implements IActorService {

    @Autowired
    public IActorRepository actorRepository;


    @Override
    public List<Actor> findAll() {
        return (List<Actor>) actorRepository.findAll();
    }

    @Override
    public List<Actor> findAllById(List<Long> ids) {
        return (List<Actor>) actorRepository.findAllById(ids);

    }

}
