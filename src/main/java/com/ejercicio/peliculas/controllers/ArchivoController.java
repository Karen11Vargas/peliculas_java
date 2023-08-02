package com.ejercicio.peliculas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ejercicio.peliculas.services.IArchivoService;

@Controller
public class ArchivoController {
    @Autowired
    private IArchivoService service;

    @GetMapping("/archivo")
    public ResponseEntity<Resource> get(@RequestParam("n") String archivo){
        return service.get(archivo);

    }
}
