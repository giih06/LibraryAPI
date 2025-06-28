package io.github.giih06.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * DTO responsável por representar os dados de um usuário na API.
 *
 * Utilizado tanto para entrada (criação/autenticação) quanto saída de informações
 * relacionadas ao usuário, com validações aplicadas aos campos obrigatórios.
 */
@Schema(name = "Usuario")
public record UsuarioDTO(
        @NotBlank(message = "Login é obrigatório!")
        String login,
        @NotBlank(message = "Senha é obrigatório!")
        String senha,
        @Email(message = "email inválido")
        @NotBlank(message = "Email é obrigatório!")
        String email,
        List<String> roles
) {
}
