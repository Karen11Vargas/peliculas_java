package com.ejercicio.peliculas.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ejercicio.peliculas.entities.Actor;
import com.ejercicio.peliculas.entities.Peliculas;
import com.ejercicio.peliculas.services.IActorService;
import com.ejercicio.peliculas.services.IGeneroService;
import com.ejercicio.peliculas.services.IPeliculaService;

@Controller
public class PeliculasController {

    private IPeliculaService service;
    private IGeneroService generoService;
    private IActorService actorService;

    public PeliculasController(IPeliculaService service, IGeneroService generoService, IActorService actorService)  {
        this.service = service;
        this.generoService = generoService;
        this.actorService = actorService;

    }

    @GetMapping("/pelicula")
    public String crear(Model model){
        Peliculas peliculas = new Peliculas();
        model.addAttribute("pelicula", peliculas);
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        model.addAttribute("titulo", "Nueva Pelicula");
        return "pelicula";
    }

    @GetMapping("/pelicula/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model){
        Peliculas peliculas = new Peliculas();
        model.addAttribute("pelicula", peliculas);
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        model.addAttribute("titulo", "Editar Pelicula");
        return "pelicula";
    }

    @PostMapping("/pelicula")
    public String guardar(Peliculas peliculas,  @ModelAttribute(name="ids") String ids) {

        List<Long> idsActores = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<Actor> protagonistas = actorService.findAllById(idsActores);
        peliculas.setProtagonistas(protagonistas);

        service.save(peliculas);
        return "redirect:home";
    }

    @GetMapping({"/", "/home", "/index"})
    public String home(){
        return "home";
    }

}
