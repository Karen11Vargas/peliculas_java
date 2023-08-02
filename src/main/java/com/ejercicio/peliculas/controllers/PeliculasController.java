package com.ejercicio.peliculas.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ejercicio.peliculas.entities.Actor;
import com.ejercicio.peliculas.entities.Peliculas;
import com.ejercicio.peliculas.services.IActorService;
import com.ejercicio.peliculas.services.IArchivoService;
import com.ejercicio.peliculas.services.IGeneroService;
import com.ejercicio.peliculas.services.IPeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculasController {

    private IPeliculaService service;
    private IGeneroService generoService;
    private IActorService actorService;
    private IArchivoService archivoService;

    public PeliculasController(IPeliculaService service, IGeneroService generoService, IActorService actorService,
            IArchivoService archivoService) {
        this.service = service;
        this.generoService = generoService;
        this.actorService = actorService;
        this.archivoService = archivoService;

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
            @ModelAttribute(name = "ids") String ids, Model model, @RequestParam("archivo") MultipartFile imagen) {

        // Validacion Campos
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nueva Pelicula");
            model.addAttribute("generos", generoService.findAll());
            model.addAttribute("actores", actorService.findAll());
            return "pelicula";
        }

        // Si se recibe imagen
        if (!imagen.isEmpty()) {
            System.out.println("Imagen no está vacía");
            String archivo = peliculas.getNombre() + getExtension(imagen.getOriginalFilename());
            System.out.println("Nombre de archivo: " + archivo);
            peliculas.setImagen(archivo);
            try {
                archivoService.guardar(archivo, imagen.getInputStream());
                System.out.println("Imagen guardada exitosamente");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Imagen está vacía");
            peliculas.setImagen("default.jpg");
        }

        // Lista Actores
        List<Long> idsActores = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<Actor> protagonistas = actorService.findAllById(idsActores);
        peliculas.setProtagonistas(protagonistas);

        // Guardar Pelicula
        service.save(peliculas);

        // Sweet Alert
        String mensaje = "La película se ha agregado exitosamente";
        String script = "Swal.fire({" +
                "    title: '¡Éxito!', " +
                "    text: '" + mensaje + "', " +
                "    icon: 'success', " +
                "    confirmButtonText: 'Aceptar'" +
                "});";

        model.addAttribute("scriptCrear", script);

        return "pelicula";
    }

    private String getExtension(String archivo) {
        return archivo.substring(archivo.lastIndexOf("."));
    }

    @GetMapping({ "/", "/home", "/index" })
    public String home(Model model) {
        model.addAttribute("peliculas", service.findAll());
        return "home";
    }

}
