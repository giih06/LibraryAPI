package io.github.giih06.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {

    // Para declarar um metodo com corpo dentro de uma interface, é obrigatório declará-lo como default
    default URI gerarHeaderLocation(UUID id) {
        // Representa a url de resposta, exemplo: http://localhost:8080/autores/7812e-323ad-232a
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
