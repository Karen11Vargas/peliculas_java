package com.ejercicio.peliculas.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ejercicio.peliculas.entities.Peliculas;

public interface IPeliculaService {
    
    public void save(Peliculas peliculas);
    public Peliculas findById(Long id);
    public List<Peliculas> findAll();
    public Page<Peliculas> findAll(PageRequest pr);
    public void delete(Long id);
}
