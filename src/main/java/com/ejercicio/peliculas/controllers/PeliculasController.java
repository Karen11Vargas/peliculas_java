package com.ejercicio.peliculas.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ejercicio.peliculas.entities.Actor;
import com.ejercicio.peliculas.entities.Peliculas;
import com.ejercicio.peliculas.services.IActorService;
import com.ejercicio.peliculas.services.IGeneroService;
import com.ejercicio.peliculas.services.IPeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculasController {

    private IPeliculaService service;
    private IGeneroService generoService;
    private IActorService actorService;

    public PeliculasController(IPeliculaService service, IGeneroService generoService, IActorService actorService) {
        this.service = service;
        this.generoService = generoService;
        this.actorService = actorService;

    }

    @GetMapping("/pelicula")
    public String crear(Model model) {
        Peliculas peliculas = new Peliculas();
        model.addAttribute("pelicula", peliculas);
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        model.addAttribute("titulo", "Nueva Pelicula");
        return "pelicula";
    }

    @GetMapping("/pelicula/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model) {
        Peliculas peliculas = new Peliculas();
        model.addAttribute("pelicula", peliculas);
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("actores", actorService.findAll());
        model.addAttribute("titulo", "Editar Pelicula");
        return "pelicula";
    }

    @PostMapping("/pelicula")
    public String guardar(@Valid @ModelAttribute("pelicula") Peliculas peliculas, BindingResult result,
            @ModelAttribute(name = "ids") String ids, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("generos", generoService.findAll());
            model.addAttribute("actores", actorService.findAll());
            return "pelicula";
        }

        List<Long> idsActores = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<Actor> protagonistas = actorService.findAllById(idsActores);
        peliculas.setProtagonistas(protagonistas);

        service.save(peliculas);

        //Sweet Alert
        // Mostrar SweetAlert
        String mensaje = "La película se ha agregado exitosamente";
        String script = "Swal.fire({" +
                "    title: '¡Éxito!', " +
                "    text: '" + mensaje + "', " +
                "    icon: 'success', " +
                "    confirmButtonText: 'Aceptar'" +
        "});" ;
                
        model.addAttribute("scriptCrear", script);

        return "home";
    }

    @GetMapping({ "/", "/home", "/index" })
    public String home(Model model) {
       
        return "home";
    }

}
