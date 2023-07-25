package com.ejercicio.peliculas.dao;

import com.ejercicio.peliculas.entities.Genero;

public interface IGeneroRepository {
    
    public void save(Genero genero);
    public Genero findById(Long id);

}
