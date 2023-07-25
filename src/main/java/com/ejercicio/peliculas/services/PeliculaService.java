package com.ejercicio.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ejercicio.peliculas.dao.IPeliculaRepository;
import com.ejercicio.peliculas.entities.Peliculas;

@Service
public class PeliculaService implements IPeliculaService {

    @Autowired
    private IPeliculaRepository peliculaRepository;

    @Override
    public void save(Peliculas peliculas) {
        peliculaRepository.save(peliculas);
    }

    @Override
    public Peliculas findById(Long id) {
        return peliculaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Peliculas> findAll() {
        return (List<Peliculas>) peliculaRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        peliculaRepository.deleteById(id);
    }
    
}
