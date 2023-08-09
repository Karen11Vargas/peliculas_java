package com.ejercicio.peliculas.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Peliculas peliculas = service.findById(id);

        String ids = "";

        for (Actor actor : peliculas.getProtagonistas()) {
            if ("".equals(ids)) {
                ids = actor.getId().toString();
            } else {
                ids = ids + "," + actor.getId().toString();
            }
        }

        model.addAttribute("pelicula", peliculas);
        model.addAttribute("ids", ids);
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
            String archivo = peliculas.getNombre() + getExtension(imagen.getOriginalFilename());
            peliculas.setImagen(archivo);
            try {
                archivoService.guardar(archivo, imagen.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            peliculas.setImagen("default.jpg");
        }

        if (ids != null && !"".equals(ids)) {
            // Lista Actores
            List<Long> idsActores = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Actor> protagonistas = actorService.findAllById(idsActores);
            peliculas.setProtagonistas(protagonistas);
        }

        String mensaje = "";
        if (peliculas.getId() == null) {
            mensaje = "La pelicula se agrego correctamente";
        } else {
            mensaje = "La pelicula se actualizo correctamente";
        }

        String script = "Swal.fire({" +
                "    title: '¡Éxito!', " +
                "    text: '" + mensaje + "', " +
                "    icon: 'success', " +
                "    confirmButtonText: 'Aceptar'" +
                "}).then(function() {" +
                "    window.location.href = '/home';" +
                "});";

        model.addAttribute("scriptCrear", script);

        // Guardar Pelicula
        service.save(peliculas);

        return "pelicula";
    }

    private String getExtension(String archivo) {
        return archivo.substring(archivo.lastIndexOf("."));
    }

    @GetMapping({ "/", "/home", "/index" })
    public String home(Model model,
            @RequestParam(name = "pagina", required = false, defaultValue = "0") Integer pagina) {

        PageRequest pr = PageRequest.of(pagina, 6);
        Page<Peliculas> page = service.findAll(pr);

        model.addAttribute("peliculas", page.getContent());

        if (page.getTotalPages() > 0) {
            List<Integer> paginas = IntStream.rangeClosed(1, page.getTotalPages()).boxed().toList();
            model.addAttribute("paginas", paginas);
        }

        model.addAttribute("actual", pagina + 1);
        model.addAttribute("titulo", "Catalogo de peliculas");
        return "home";
    }

    @GetMapping({ "/listado" })
    public String listado(Model model) {
        model.addAttribute("titulo", "Listado de Peliculas");
        model.addAttribute("peliculas", service.findAll());

        return "listado";
    }

    @GetMapping("/pelicula/{id}/delete")
    public String eliminar(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirect) {

        String mensaje = "Se ha eliminado la pelicula #" + id;

        String script = "Swal.fire({" +
                "    title: '¡Éxito!', " +
                "    text: '" + mensaje + "', " +
                "    icon: 'success', " +
                "    confirmButtonText: 'Aceptar'" +
                "}).then(function() {" +
                "    window.location.href = '/listado';" +
                "});";

        model.addAttribute("scriptEdit", script);

        service.delete(id);

        return "listado";
    }
}
