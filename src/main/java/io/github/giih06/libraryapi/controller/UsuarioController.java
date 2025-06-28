package io.github.giih06.libraryapi.controller;

import io.github.giih06.libraryapi.controller.dto.UsuarioDTO;
import io.github.giih06.libraryapi.controller.mappers.UsuarioMapper;
import io.github.giih06.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável por operações relacionadas aos usuários do sistema.
 * Permite o cadastro de novos usuários via DTO validado.
 */
@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios") // Tag de agrupamento para documentação Swagger
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    /**
     * Endpoint responsável por cadastrar um novo usuário.
     * Requer payload válido com os dados do usuário.
     *
     * @param dto objeto contendo os dados do usuário a ser cadastrado
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar", description = "Cadastrar novo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Usuario já cadastrado.")
    })
    public void salvar(@RequestBody @Valid UsuarioDTO dto) {
        // Converte o DTO em entidade de domínio
        var usuario = mapper.toEntity(dto);

        service.salvar(usuario);
    }
}
