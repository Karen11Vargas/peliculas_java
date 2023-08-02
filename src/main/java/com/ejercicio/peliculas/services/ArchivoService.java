package com.ejercicio.peliculas.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ArchivoService implements IArchivoService {

    @Override
    public void guardar(String archivo, InputStream bytes) {
        try {
            eliminar(archivo);
            Files.copy(bytes, resolvePath(archivo));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ResponseEntity<Resource> get(String archivo) {
        Resource resourse = null;
        try {
            resourse = new UrlResource(resolvePath(archivo).toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + resourse.getFilename() + "\"")
                .body(resourse);
    }

    private Path resolvePath(String archivo) {
        return Paths.get("archivos").resolve(archivo).toAbsolutePath();
    }

    @Override
    public void eliminar(String archivo) {
        try {
            Files.deleteIfExists(resolvePath(archivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
