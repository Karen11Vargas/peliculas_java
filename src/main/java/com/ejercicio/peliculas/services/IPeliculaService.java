package com.ejercicio.peliculas.services;

import java.util.List;

import com.ejercicio.peliculas.entities.Peliculas;

public interface IPeliculaService {
    
    public void save(Peliculas peliculas);
    public Peliculas findById(Long id);
    public List<Peliculas> findAll();
    public void delete(Long id);
}
