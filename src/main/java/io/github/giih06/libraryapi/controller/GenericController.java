package io.github.giih06.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Interface genérica para controladores REST que provê utilitários comuns.
 * Pode ser implementada por qualquer controller que precise gerar a URI do recurso criado.
 */
public interface GenericController {


    /**
     * Gera uma URI de localização para o recurso recém-criado com base na requisição atual.
     * Útil para ser utilizada no cabeçalho `Location` em respostas HTTP 201 (Created).
     *
     * @param id UUID do recurso recém-criado
     * @return URI no formato: http://host:porta/recurso/{id}
     */
    default URI gerarHeaderLocation(UUID id) {
        // Constrói a URI com base no endpoint atual e adiciona o ID ao final do path
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
