package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.model.Client;
import io.github.giih06.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelo gerenciamento de clientes OAuth2.
 * Fornece endpoint para cadastrar um novo client.
 */
@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
@Tag(name = "Clients") // Tag de agrupamento para documentação Swagger
@Slf4j
public class ClientController {

    private final ClientService service;

    /**
     * Endpoint para salvar um novo cliente OAuth2.
     * Acesso restrito a usuários com o papel GERENTE.
     *
     * @param client objeto contendo as informações do cliente a ser cadastrado
     *
     * @status 201 - Cliente cadastrado com sucesso
     * @status 422 - Erros de validação do corpo da requisição
     * @status 409 - Cliente já existente com o mesmo client_id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado.")
    })
    public void salvar(@RequestBody Client client) {
        log.info("Registrando novo Client: {} com scope: {} ", client.getClientId(), client.getScope());

        // Chamada para o serviço de persistência de client
        service.salvar(client);
    }
}
